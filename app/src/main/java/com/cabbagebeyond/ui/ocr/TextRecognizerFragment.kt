package com.cabbagebeyond.ui.ocr

import android.Manifest
import android.app.Activity.RESULT_OK
import android.app.AlertDialog
import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import com.cabbagebeyond.R
import com.cabbagebeyond.databinding.FragmentTextRecognizerBinding
import com.cabbagebeyond.util.CollectionProperty


class TextRecognizerFragment : Fragment() {

    private lateinit var _propertyKeys: Array<CollectionProperty>

    private lateinit var _viewModel: TextRecognizerViewModel
    private lateinit var _binding: FragmentTextRecognizerBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _propertyKeys = TextRecognizerFragmentArgs.fromBundle(requireArguments()).properties

        _binding = FragmentTextRecognizerBinding.inflate(inflater)

        _viewModel = TextRecognizerViewModel()

        _binding.lifecycleOwner = this
        _binding.viewModel = _viewModel

        _binding.cameraButton.setOnClickListener {
            checkCameraPermission()
        }

        _binding.galleryButton.setOnClickListener {
            startChooseImageFromGalleryIntent()
        }

        _binding.recognizedTextView.customSelectionActionModeCallback = AddToPropertyActionCallback(_binding.recognizedTextView) {
            showChooseDialog(it)
        }

        return _binding.root
    }


    private fun checkCameraPermission() {
        val cameraPermission = Manifest.permission.CAMERA
        when {
            isPermissionGranted(cameraPermission) -> {
                startCameraIntent()
            }
            shouldShowRequestPermissionRationale(cameraPermission) -> {
                // In an educational UI, explain to the user why your app requires this
                // permission for a specific feature to behave as expected. In this UI,
                // include a "cancel" or "no thanks" button that allows the user to
                // continue using your app without granting the permission.
                Log.d(TAG, "show explanation")
            }
            else -> {
                requestPermissions(arrayOf(cameraPermission), PERMISSION_CAMERA_REQUESTS)
            }
        }
    }

    private fun startCameraIntent() {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        try {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
        } catch (e: ActivityNotFoundException) {
            Log.e(TAG, "Failed to start camera intent", e)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            val uri = data?.extras?.get("data") as Uri
            _viewModel.startTextRecognition(requireContext(), uri)
        }
        else if (requestCode == REQUEST_CHOOSE_IMAGE && resultCode == RESULT_OK) {
            val uri = data?.data as Uri
            _viewModel.startTextRecognition(requireContext(), uri)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>, grantResults: IntArray
    ) {
        when (requestCode) {
            PERMISSION_CAMERA_REQUESTS -> {
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    startCameraIntent()
                } else {
                    Log.d(TAG, "CAMERA permission denied")
                }
            }

            // Add other 'when' lines to check for other
            // permissions this app might request.
            else -> {
                // Ignore all other requests.
            }
        }
    }

    private fun isPermissionGranted(permission: String): Boolean {
        return (ContextCompat.checkSelfPermission(
            requireContext(),
            permission
        ) == PackageManager.PERMISSION_GRANTED)
    }

    private fun startChooseImageFromGalleryIntent() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"
        startActivityForResult(
            Intent.createChooser(intent, "Select Picture"),
            REQUEST_CHOOSE_IMAGE
        )
    }

    private fun showChooseDialog(value: String) {
        var selectedKey = _propertyKeys.first()
        val keys = _propertyKeys.map { resources.getString(it.displayName) }.toTypedArray()

        AlertDialog
            .Builder(requireContext())
            .setTitle(R.string.ocr_add_property_dialog_title)
            .setSingleChoiceItems(keys, 0) { _, i ->
                selectedKey = _propertyKeys[i]
            }
            .setPositiveButton(R.string.ocr_add_property_dialog_add_button) { _, _ ->
                addProperty(selectedKey, value)
            }
            .create()
            .show()
    }

    private fun addProperty(property: CollectionProperty, value: String) {
        property.value = value

        val bundle = Bundle()
        bundle.putParcelableArray(RESULT_ARRAY_KEY, _propertyKeys)
        setFragmentResult(RESULT_KEY, bundle)
    }

    companion object {
        private const val TAG = "TextRecognizerFragment"
        private const val REQUEST_IMAGE_CAPTURE = 1001
        private const val REQUEST_CHOOSE_IMAGE = 1002
        private const val PERMISSION_CAMERA_REQUESTS = 1101

        const val RESULT_KEY = "requestKey"
        const val RESULT_ARRAY_KEY = "array"
    }
}
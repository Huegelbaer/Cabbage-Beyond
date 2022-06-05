package com.cabbagebeyond.ui.admin.roles.edit

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.LinearLayoutManager
import com.cabbagebeyond.R
import com.cabbagebeyond.databinding.FragmentRoleEditBinding
import com.cabbagebeyond.util.Feature

class RoleEditDialogFragment : DialogFragment(), AdapterView.OnItemSelectedListener {

    private var _name: String = ""
    private var _selectedFeatures = MutableLiveData<List<Feature>>()
    private var _unselectedFeatures: MutableList<Feature> = mutableListOf()

    private lateinit var _binding: FragmentRoleEditBinding
    private lateinit var _adapter: RoleEditAdapter

    var onSave: ((name: String, selectedFeatures: List<String>) -> Unit)? = null
    var onDelete: (() -> Unit)? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        arguments?.let { args ->
            _name = args.getString(ARG_PARAM_NAME) ?: ""
            val features = args.getStringArray(ARG_PARAM_FEATURES) ?: arrayOf()
            _selectedFeatures.value = features.map { Feature.valueOf(it) }
        }

        return AlertDialog.Builder(requireContext())
            .setView(setupView(layoutInflater))
            .setPositiveButton(R.string.dialog_button_save) { _, _ ->
                val features = _selectedFeatures.value?.map { it.name }
                onSave?.let { it(_name, features ?: listOf()) }
            }
            .setNegativeButton(R.string.dialog_button_delete) { _, _ ->
                onDelete?.let { it() }
            }
            .setNeutralButton(R.string.dialog_button_cancel) { _, _ -> }
            .create()
    }

    private fun setupView(inflater: LayoutInflater): View {
        _binding = FragmentRoleEditBinding.inflate(inflater)

        setupEditText()
        setupSpinner()
        setupButton()
        setupRecyclerView()

        return _binding.root
    }

    private fun setupEditText() {
        _binding.editRoleName.setText(_name, TextView.BufferType.EDITABLE)
        _binding.editRoleName.setOnFocusChangeListener { view, hasFocus ->
            if (!hasFocus) {
                _name = (view as EditText).text.toString()
            }
        }
    }

    private fun setupSpinner() {
        val selected = _selectedFeatures.value ?: listOf()
        _unselectedFeatures = Feature.values().filter { feature ->
            !selected.contains(feature)
        }.toMutableList()

        ArrayAdapter(
            requireContext(),
            R.layout.spinner_item,
            _unselectedFeatures
        ).also { adapter ->
            adapter.setDropDownViewResource(R.layout.spinner_dropdown_item)
            _binding.spinnerFeatures.adapter = adapter
        }
    }

    private fun setupButton() {
        _binding.addFeatureButton.setOnClickListener {
            _binding.spinnerFeatures.selectedItem?.let {
                val feature = it as Feature
                _unselectedFeatures.remove(feature)
                val selectedFeatures = _selectedFeatures.value?.toMutableList() ?: mutableListOf()
                selectedFeatures.add(feature)
                _selectedFeatures.value = selectedFeatures
            }
        }
    }

    private fun setupRecyclerView() {
        val clickListener = FeatureClickListener {
            _selectedFeatures.value?.toMutableList()?.let { list ->
                list.remove(it)
                _selectedFeatures.value = list
                _unselectedFeatures.add(it)
            }
        }
        _adapter = RoleEditAdapter(clickListener)

        _binding.featureList.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = _adapter
        }

        _selectedFeatures.observe(this) {
            _adapter.submitList(it)
        }
    }

    companion object {
        const val TAG = "RoleEditFragment"
        const val ARG_PARAM_NAME = "name"
        const val ARG_PARAM_FEATURES = "features"

        fun newInstance(name: String, features: List<String>): RoleEditDialogFragment {
            val fragment = RoleEditDialogFragment()
            val bundle = Bundle()
            bundle.putString(ARG_PARAM_NAME, name)
            bundle.putStringArray(ARG_PARAM_FEATURES, features.toTypedArray())
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
        _binding.addFeatureButton.isEnabled = true
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {
        _binding.addFeatureButton.isEnabled = false
    }
}
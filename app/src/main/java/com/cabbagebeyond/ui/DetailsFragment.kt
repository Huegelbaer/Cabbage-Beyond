package com.cabbagebeyond.ui

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import com.cabbagebeyond.R
import com.cabbagebeyond.ui.ocr.TextRecognizerFragment
import com.cabbagebeyond.util.CollectionProperty
import com.google.android.material.snackbar.Snackbar

open class DetailsFragment : Fragment() {

    protected open lateinit var viewModel: DetailsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Use the Kotlin extension in the fragment-ktx artifact
        setFragmentResultListener(TextRecognizerFragment.RESULT_KEY) { _, bundle ->
            val properties = bundle.getParcelableArray(TextRecognizerFragment.RESULT_ARRAY_KEY) ?: return@setFragmentResultListener
            val array = properties.mapNotNull { it as CollectionProperty }.toTypedArray()
            viewModel.onPropertiesReceived(array)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.edit, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.menu_ocr) {
            navigateToOcr()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    protected open fun navigateToOcr() {}

    protected fun setupSpinner(attribute: String?, attributes: List<String>, spinner: Spinner, onSelected: ((index: Int) -> Unit)) {
        setupSpinner(attribute, attributes, spinner, object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parentView: AdapterView<*>?,
                selectedItemView: View?,
                position: Int,
                id: Long
            ) {
                onSelected(position)
            }

            override fun onNothingSelected(parentView: AdapterView<*>?) {
                // your code here
            }
        })
    }

    private fun setupSpinner(
        selectedItem: Any?,
        items: List<Any?>,
        spinner: Spinner,
        listener: AdapterView.OnItemSelectedListener
    ) {
        ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item,
            items
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner.adapter = adapter
        }
        val position = items.indexOf(selectedItem)
        spinner.setSelection(position)

        spinner.onItemSelectedListener = listener
    }

    protected fun showSnackbar(message: String) {
        Snackbar
            .make(requireView(), message, Snackbar.LENGTH_LONG)
            .show()
    }

    protected fun toggleVisibility(readGroup: View, editGroup: View, isEditing: Boolean) {
        if (isEditing) {
            readGroup.visibility = View.INVISIBLE
            editGroup.visibility = View.VISIBLE
        } else {
            readGroup.visibility = View.VISIBLE
            editGroup.visibility = View.INVISIBLE
        }
    }
}
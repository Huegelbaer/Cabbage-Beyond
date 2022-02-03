package com.cabbagebeyond.ui

import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.fragment.app.Fragment
import com.cabbagebeyond.R
import com.cabbagebeyond.model.World
import com.google.android.material.snackbar.Snackbar

open class DetailsFragment : Fragment() {

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

    protected fun setupStringSpinner(attribute: String, attributes: List<String>, spinner: Spinner, onSelected: ((item: String) -> Unit)) {
        setupSpinner(attribute, attributes, spinner, object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parentView: AdapterView<*>?,
                selectedItemView: View?,
                position: Int,
                id: Long
            ) {
                val item = attributes[position]
                onSelected(item)
            }

            override fun onNothingSelected(parentView: AdapterView<*>?) {
                // your code here
            }
        })
    }

    protected fun setupWorldSpinner(world: World?, worlds: List<World?>, spinner: Spinner, onSelected: ((item: World?) -> Unit)) {
        val titles = worlds.map { it?.name ?: "" }

        setupSpinner(world?.name, titles, spinner, object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parentView: AdapterView<*>?,
                selectedItemView: View?,
                position: Int,
                id: Long
            ) {
                val item = worlds[position]
                onSelected(item)
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
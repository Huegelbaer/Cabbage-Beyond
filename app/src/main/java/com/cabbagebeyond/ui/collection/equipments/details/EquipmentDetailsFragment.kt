package com.cabbagebeyond.ui.collection.equipments.details

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.lifecycle.Observer
import com.cabbagebeyond.R
import com.cabbagebeyond.data.EquipmentDataSource
import com.cabbagebeyond.data.WorldDataSource
import com.cabbagebeyond.databinding.EquipmentDetailsFragmentBinding
import com.cabbagebeyond.model.User
import com.cabbagebeyond.model.World
import com.cabbagebeyond.ui.collection.abilities.details.AbilityDetailsFragmentArgs
import com.cabbagebeyond.ui.collection.abilities.details.AbilityDetailsViewModel
import com.cabbagebeyond.util.Feature
import com.google.android.material.snackbar.Snackbar
import org.koin.android.ext.android.inject

class EquipmentDetailsFragment : Fragment() {

    companion object {
        fun newInstance() = EquipmentDetailsFragment()
    }

    private lateinit var _viewModel: EquipmentDetailsViewModel

    private lateinit var _binding: EquipmentDetailsFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = EquipmentDetailsFragmentBinding.inflate(inflater)

        val equipment = EquipmentDetailsFragmentArgs.fromBundle(requireArguments()).equipment

        val dataSource: EquipmentDataSource by inject()
        val worldDataSource: WorldDataSource by inject()
        _viewModel = EquipmentDetailsViewModel(equipment, dataSource, worldDataSource, User("", "", listOf(
            Feature.CONFIGURE_APP.name), listOf(), ""), requireContext())

        _binding.viewModel = _viewModel
        _binding.equipment = _viewModel.equipment.value

        _viewModel.equipment.observe(viewLifecycleOwner, Observer {
            it?.let {
                _binding.equipment = it
            }
        })

        _viewModel.fabImage.observe(viewLifecycleOwner, Observer {
            it?.let {
                _binding.floatingActionButton.setImageResource(it)
            }
        })

        _viewModel.isEditing.observe(viewLifecycleOwner, Observer {
            it?.let { isEditing ->
                if (isEditing) {
                    _binding.readGroup.visibility = View.INVISIBLE
                    _binding.editGroup.visibility = View.VISIBLE
                } else {
                    _binding.readGroup.visibility = View.VISIBLE
                    _binding.editGroup.visibility = View.INVISIBLE
                }
            }
        })

        _viewModel.types.observe(viewLifecycleOwner, Observer {
            val list = it ?: listOf()
            setupTypeSpinner(equipment.type, list)
        })

        _viewModel.worlds.observe(viewLifecycleOwner, Observer {
            val list = it ?: listOf()
            setupWorldSpinner(equipment.world, list)
        })

        _viewModel.message.observe(viewLifecycleOwner, Observer {
            it?.let {
                showSnackbar(resources.getString(it))
            }
        })

        return _binding.root
    }

    private fun setupTypeSpinner(attribute: String, attributes: List<String>) {
        setupSpinner(attribute, attributes, _binding.typeSpinner, object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parentView: AdapterView<*>?,
                selectedItemView: View?,
                position: Int,
                id: Long
            ) {
                val item = attributes[position]
                _viewModel.onAttributeSelected(item)
            }

            override fun onNothingSelected(parentView: AdapterView<*>?) {
                // your code here
            }
        })
    }

    private fun setupWorldSpinner(world: World?, worlds: List<World?>) {
        val titles = worlds.map { it?.name ?: "" }

        setupSpinner(world?.name, titles, _binding.worldSpinner, object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parentView: AdapterView<*>?,
                selectedItemView: View?,
                position: Int,
                id: Long
            ) {
                val item = worlds[position]
                _viewModel.onWorldSelected(item)
            }

            override fun onNothingSelected(parentView: AdapterView<*>?) {
                // your code here
            }
        })
    }

    private fun setupSpinner(selectedItem: Any?, items: List<Any?>, spinner: Spinner, listener: AdapterView.OnItemSelectedListener) {
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

    private fun showSnackbar(message: String) {
        Snackbar
            .make(requireView(), message, Snackbar.LENGTH_LONG)
            .show()
    }
}


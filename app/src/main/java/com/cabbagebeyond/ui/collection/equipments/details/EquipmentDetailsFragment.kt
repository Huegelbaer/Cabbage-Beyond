package com.cabbagebeyond.ui.collection.equipments.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.cabbagebeyond.data.EquipmentDataSource
import com.cabbagebeyond.data.WorldDataSource
import com.cabbagebeyond.databinding.EquipmentDetailsFragmentBinding
import com.cabbagebeyond.model.User
import com.cabbagebeyond.model.World
import com.cabbagebeyond.ui.DetailsFragment
import com.cabbagebeyond.util.Feature
import org.koin.android.ext.android.inject

class EquipmentDetailsFragment : DetailsFragment() {

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
            Feature.CONFIGURE_APP.name), listOf(), ""), requireActivity().application)

        _binding.viewModel = _viewModel
        _binding.lifecycleOwner = this

        _viewModel.fabImage.observe(viewLifecycleOwner) {
            it?.let {
                _binding.floatingActionButton.setImageResource(it)
            }
        }

        _viewModel.isEditing.observe(viewLifecycleOwner) {
            it?.let { isEditing ->
                toggleVisibility(_binding.readGroup, _binding.editGroup, isEditing)
            }
        }

        _viewModel.types.observe(viewLifecycleOwner) {
            setupTypeSpinner(equipment.type, it ?: listOf())
        }

        _viewModel.worlds.observe(viewLifecycleOwner) {
            setupWorldSpinner(equipment.world, it ?: listOf())
        }

        _viewModel.message.observe(viewLifecycleOwner) {
            it?.let {
                showSnackbar(resources.getString(it))
            }
        }

        return _binding.root
    }

    private fun setupTypeSpinner(type: String, types: List<String>) {
        setupStringSpinner(type, types, _binding.typeSpinner) {
            _viewModel.onTypeSelected(it)
        }
    }

    private fun setupWorldSpinner(world: World?, worlds: List<World?>) {
        super.setupWorldSpinner(world, worlds, _binding.worldSpinner) {
            _viewModel.onWorldSelected(it)
        }
    }
}


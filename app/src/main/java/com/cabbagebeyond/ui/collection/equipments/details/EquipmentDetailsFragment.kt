package com.cabbagebeyond.ui.collection.equipments.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.cabbagebeyond.data.EquipmentDataSource
import com.cabbagebeyond.data.WorldDataSource
import com.cabbagebeyond.databinding.EquipmentDetailsFragmentBinding
import com.cabbagebeyond.model.World
import com.cabbagebeyond.services.UserService
import com.cabbagebeyond.ui.DetailsFragment
import com.cabbagebeyond.ui.collection.abilities.details.AbilityDetailsViewModel
import org.koin.android.ext.android.inject

class EquipmentDetailsFragment : DetailsFragment() {

    companion object {
        fun newInstance() = EquipmentDetailsFragment()
    }

    private val _viewModel: EquipmentDetailsViewModel
        get() = viewModel as EquipmentDetailsViewModel

    private lateinit var _binding: EquipmentDetailsFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = EquipmentDetailsFragmentBinding.inflate(inflater)

        val equipment = EquipmentDetailsFragmentArgs.fromBundle(requireArguments()).equipment

        val dataSource: EquipmentDataSource by inject()
        val worldDataSource: WorldDataSource by inject()
        viewModel = EquipmentDetailsViewModel(equipment, dataSource, worldDataSource, UserService.currentUser!!, requireActivity().application)

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

        setHasOptionsMenu(true)

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

    override fun navigateToOcr() {
        findNavController().navigate(EquipmentDetailsFragmentDirections.actionEquipmentDetailsToOcr(_viewModel.properties))
    }
}


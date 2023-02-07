package com.cabbagebeyond.ui.collection.equipments.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.cabbagebeyond.data.EquipmentDataSource
import com.cabbagebeyond.data.WorldDataSource
import com.cabbagebeyond.databinding.FragmentEquipmentDetailsBinding
import com.cabbagebeyond.model.World
import com.cabbagebeyond.services.UserService
import com.cabbagebeyond.ui.DetailsFragment
import com.cabbagebeyond.ui.collection.equipments.EquipmentType
import org.koin.android.ext.android.inject

class EquipmentDetailsFragment : DetailsFragment() {

    companion object {
        fun newInstance() = EquipmentDetailsFragment()
    }

    private val _viewModel: EquipmentDetailsViewModel
        get() = viewModel as EquipmentDetailsViewModel

    private lateinit var _binding: FragmentEquipmentDetailsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentEquipmentDetailsBinding.inflate(inflater)

        val equipment = EquipmentDetailsFragmentArgs.fromBundle(requireArguments()).equipment
        val isEditingActive = EquipmentDetailsFragmentArgs.fromBundle(requireArguments()).startEditing

        val dataSource: EquipmentDataSource by inject()
        val worldDataSource: WorldDataSource by inject()
        viewModel = EquipmentDetailsViewModel(
            equipment,
            isEditingActive,
            dataSource,
            worldDataSource,
            UserService.currentUser,
            requireActivity().application
        )

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
            setupTypeSpinner(it.selected, it.values)
        }

        _viewModel.worlds.observe(viewLifecycleOwner) {
            setupWorldSpinner(it.selected, it.values)
        }

        _viewModel.message.observe(viewLifecycleOwner) {
            it?.let {
                showSnackbar(resources.getString(it))
            }
        }

        setHasOptionsMenu(true)

        return _binding.root
    }

    private fun setupTypeSpinner(preSelection: EquipmentType?, types: List<EquipmentType>) {
        setupSpinner(preSelection?.title, types.map { it.title }, _binding.typeSpinner) { index ->
            _viewModel.onTypeSelected(types[index])
        }
    }

    private fun setupWorldSpinner(preSelection: World?, worlds: List<World?>) {
        setupSpinner(preSelection?.name ?: "", worlds.mapNotNull { it?.name }, _binding.worldSpinner) {
            _viewModel.onWorldSelected(worlds[it])
        }
    }

    override fun navigateToOcr() {
        findNavController().navigate(
            EquipmentDetailsFragmentDirections.actionEquipmentDetailsToOcr(
                _viewModel.properties
            )
        )
    }
}


package com.cabbagebeyond.ui.collection.equipments

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.cabbagebeyond.FilterDialogFragment
import com.cabbagebeyond.data.EquipmentDataSource
import com.cabbagebeyond.databinding.FragmentEquipmentsListBinding
import com.cabbagebeyond.model.Equipment
import com.cabbagebeyond.model.World
import com.cabbagebeyond.ui.collection.CollectionListFragment
import com.cabbagebeyond.ui.collection.CollectionListViewModel
import org.koin.android.ext.android.inject


class EquipmentsFragment : CollectionListFragment() {

    private val _viewModel: EquipmentsViewModel
        get() = viewModel as EquipmentsViewModel

    private lateinit var _binding: FragmentEquipmentsListBinding
    private lateinit var _adapter: EquipmentsRecyclerViewAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEquipmentsListBinding.inflate(inflater)

        val dataSource: EquipmentDataSource by inject()
        viewModel = EquipmentsViewModel(requireActivity().application, dataSource)

        val clickListener = EquipmentClickListener {
            _viewModel.onEquipmentClicked(it)
        }
        _adapter = EquipmentsRecyclerViewAdapter(clickListener)

        _binding.list.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = _adapter
        }

        _viewModel.items.observe(viewLifecycleOwner) {
            it?.let {
                _adapter.submitList(it)
            }
        }

        _viewModel.selectedEquipment.observe(viewLifecycleOwner) {
            it?.let {
                showDetails(it)
            }
        }

        _viewModel.interaction.observe(viewLifecycleOwner) {
            it?.let {
                when (it) {
                    is EquipmentsViewModel.Interaction.OpenFilter -> {
                        showFilterDialog(it.types, it.worlds)
                    }
                }
                _viewModel.onInteractionCompleted()
            }
        }

        setHasOptionsMenu(true)

        return _binding.root
    }

    private fun showFilterDialog(types: CollectionListViewModel.FilterData<EquipmentType>, worlds: CollectionListViewModel.FilterData<World>) {

        var selectedType = types.selected
        var selectedWorld = worlds.selected

        val dialog = FilterDialogFragment(onFilter = {
            _viewModel.filter(selectedType, selectedWorld)
        })

        dialog.addFilterChipGroup(types.title, types.values, types.selected, types.titleProperty) {
            selectedType = it
        }
        dialog.addFilterChipGroup(worlds.title, worlds.values, worlds.selected, worlds.titleProperty) {
            selectedWorld = it
        }

        dialog.show(requireActivity().supportFragmentManager, "talent_dialog_filter")
    }

    private fun showDetails(equipment: Equipment) {
        findNavController().navigate(EquipmentsFragmentDirections.actionEquipmentsToDetails(equipment))
        _viewModel.onNavigationCompleted()
    }
}
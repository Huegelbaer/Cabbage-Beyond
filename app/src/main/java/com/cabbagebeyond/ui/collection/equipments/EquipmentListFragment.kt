package com.cabbagebeyond.ui.collection.equipments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.cabbagebeyond.EmptyListStateModel
import com.cabbagebeyond.FilterDialogFragment
import com.cabbagebeyond.data.EquipmentDataSource
import com.cabbagebeyond.databinding.FragmentEquipmentListBinding
import com.cabbagebeyond.model.Equipment
import com.cabbagebeyond.model.World
import com.cabbagebeyond.services.UserService
import com.cabbagebeyond.ui.collection.CollectionListFragment
import com.cabbagebeyond.ui.collection.CollectionListViewModel
import org.koin.android.ext.android.inject


class EquipmentListFragment : CollectionListFragment<Equipment>() {

    private val _viewModel: EquipmentListViewModel
        get() = viewModel as EquipmentListViewModel

    private lateinit var _binding: FragmentEquipmentListBinding
    private lateinit var _adapter: EquipmentListAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEquipmentListBinding.inflate(inflater)

        val dataSource: EquipmentDataSource by inject()
        viewModel =
            EquipmentListViewModel(UserService.currentUser, requireActivity().application, dataSource)

        val clickListener = EquipmentClickListener {
            _viewModel.onItemSelected(it)
        }
        _adapter = EquipmentListAdapter(clickListener)

        _binding.list.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = _adapter
        }

        _binding.floatingActionButton.setOnClickListener {
            _viewModel.addEquipment()
        }

        _viewModel.userCanAddNewContent.observe(viewLifecycleOwner) { canAddContent ->
            canAddContent?.let {
                _binding.floatingActionButton.visibility = if (it) View.VISIBLE else View.GONE
            }
        }

        setupViewModelObservers()

        setHasOptionsMenu(true)

        return _binding.root
    }

    override fun setupViewModelObservers() {
        super.setupViewModelObservers()

        _viewModel.items.observe(viewLifecycleOwner) {
            it?.let {
                _adapter.submitList(it)
            }
        }

        _viewModel.selectedItem.observe(viewLifecycleOwner) {
            it?.let {
                showDetails(it.first, it.second)
            }
        }

        _viewModel.interaction.observe(viewLifecycleOwner) {
            it?.let {
                when (it) {
                    is EquipmentListViewModel.Interaction.OpenFilter -> {
                        showFilterDialog(it.types, it.worlds)
                    }
                }
                _viewModel.onInteractionCompleted()
            }
        }
    }

    override fun showEmptyState(
        title: String,
        message: String,
        buttonTitle: String?,
        action: (() -> Unit)?
    ) {
        _binding.list.visibility = View.GONE
        _binding.emptyStateView.root.visibility = View.VISIBLE
        _binding.emptyStateView.model = EmptyListStateModel(title, message, buttonTitle) {
            action?.let { it() }
        }
    }

    override fun showList() {
        _binding.list.visibility = View.VISIBLE
        _binding.emptyStateView.root.visibility = View.GONE
    }

    private fun showFilterDialog(
        types: CollectionListViewModel.FilterData<EquipmentType>,
        worlds: CollectionListViewModel.FilterData<World>
    ) {

        var selectedType = types.selected
        var selectedWorld = worlds.selected

        val dialog = FilterDialogFragment(onFilter = {
            _viewModel.filter(selectedType, selectedWorld)
        })

        dialog.addFilterChipGroup(types.title, types.values, types.selected, types.titleProperty) {
            selectedType = it
        }
        dialog.addFilterChipGroup(
            worlds.title,
            worlds.values,
            worlds.selected,
            worlds.titleProperty
        ) {
            selectedWorld = it
        }

        dialog.show(requireActivity().supportFragmentManager, "talent_dialog_filter")
    }

    private fun showDetails(equipment: Equipment, startEditing: Boolean) {
        findNavController().navigate(
            EquipmentListFragmentDirections.actionEquipmentsToDetails(
                equipment,
                startEditing
            )
        )
        _viewModel.onNavigationCompleted()
    }
}
package com.cabbagebeyond.ui.collection.handicaps

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.cabbagebeyond.FilterDialogFragment
import com.cabbagebeyond.data.HandicapDataSource
import com.cabbagebeyond.databinding.FragmentHandicapsListBinding
import com.cabbagebeyond.model.Handicap
import com.cabbagebeyond.model.World
import com.cabbagebeyond.ui.collection.CollectionListFragment
import com.cabbagebeyond.ui.collection.CollectionListViewModel
import org.koin.android.ext.android.inject

class HandicapsFragment : CollectionListFragment() {

    private val _viewModel: HandicapsViewModel
        get() = viewModel as HandicapsViewModel

    private lateinit var _binding: FragmentHandicapsListBinding
    private lateinit var _adapter: HandicapsRecyclerViewAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHandicapsListBinding.inflate(inflater)

        val dataSource: HandicapDataSource by inject()
        viewModel = HandicapsViewModel(requireActivity().application, dataSource)

        val clickListener = HandicapClickListener {
            _viewModel.onHandicapClicked(it)
        }
        _adapter = HandicapsRecyclerViewAdapter(clickListener)

        _binding.list.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = _adapter
        }

        _viewModel.items.observe(viewLifecycleOwner) {
            it?.let {
                _adapter.submitList(it)
            }
        }

        _viewModel.selectedHandicap.observe(viewLifecycleOwner) {
            it?.let {
                showDetails(it)
            }
        }

        _viewModel.interaction.observe(viewLifecycleOwner) {
            it?.let {
                when (it) {
                    is HandicapsViewModel.Interaction.OpenFilter -> {
                        showFilterDialog(it.types, it.worlds)
                    }
                }
                _viewModel.onInteractionCompleted()
            }
        }

        setHasOptionsMenu(true)

        return _binding.root
    }

    private fun showFilterDialog(types: CollectionListViewModel.FilterData<HandicapType>, worlds: CollectionListViewModel.FilterData<World>) {

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

    private fun showDetails(handicap: Handicap) {
        findNavController().navigate(HandicapsFragmentDirections.actionHandicapsToDetails(handicap))
        _viewModel.onNavigationCompleted()
    }
}
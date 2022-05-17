package com.cabbagebeyond.ui.collection.races

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.cabbagebeyond.FilterDialogFragment
import com.cabbagebeyond.data.RaceDataSource
import com.cabbagebeyond.databinding.FragmentRacesListBinding
import com.cabbagebeyond.model.Race
import com.cabbagebeyond.model.World
import com.cabbagebeyond.ui.collection.CollectionListFragment
import com.cabbagebeyond.ui.collection.CollectionListViewModel
import org.koin.android.ext.android.inject

class RacesFragment : CollectionListFragment() {

    private val _viewModel: RacesViewModel
        get() = viewModel as RacesViewModel

    private lateinit var _binding: FragmentRacesListBinding
    private lateinit var _adapter: RacesRecyclerViewAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRacesListBinding.inflate(inflater)

        val dataSource: RaceDataSource by inject()
        viewModel = RacesViewModel(requireActivity().application, dataSource)

        val clickListener = RaceClickListener {
            _viewModel.onRaceClicked(it)
        }
        _adapter = RacesRecyclerViewAdapter(clickListener)

        _binding.list.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = _adapter
        }

        _viewModel.items.observe(viewLifecycleOwner) {
            it?.let {
                _adapter.submitList(it)
            }
        }

        _viewModel.selectedRace.observe(viewLifecycleOwner) {
            it?.let {
                showDetails(it)
            }
        }

        _viewModel.interaction.observe(viewLifecycleOwner) {
            it?.let {
                when (it) {
                    is RacesViewModel.Interaction.OpenFilter -> {
                        showFilterDialog(it.worlds)
                    }
                }
                _viewModel.onInteractionCompleted()
            }
        }

        setHasOptionsMenu(true)

        return _binding.root
    }

    private fun showFilterDialog(worlds: CollectionListViewModel.FilterData<World>) {

        var selectedWorld = worlds.selected

        val dialog = FilterDialogFragment(onFilter = {
            _viewModel.filter(selectedWorld)
        })

        dialog.addFilterChipGroup(worlds.title, worlds.values, worlds.selected, worlds.titleProperty) {
            selectedWorld = it
        }

        dialog.show(requireActivity().supportFragmentManager, "talent_dialog_filter")
    }

    private fun showDetails(race: Race) {
        findNavController().navigate(RacesFragmentDirections.actionRacesToDetails(race))
        _viewModel.onNavigationCompleted()
    }
}
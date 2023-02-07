package com.cabbagebeyond.ui.collection.races

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.cabbagebeyond.EmptyListStateModel
import com.cabbagebeyond.FilterDialogFragment
import com.cabbagebeyond.data.RaceDataSource
import com.cabbagebeyond.databinding.FragmentRaceListBinding
import com.cabbagebeyond.model.Race
import com.cabbagebeyond.model.World
import com.cabbagebeyond.services.UserService
import com.cabbagebeyond.ui.collection.CollectionListFragment
import com.cabbagebeyond.ui.collection.CollectionListViewModel
import org.koin.android.ext.android.inject

class RaceListFragment : CollectionListFragment<Race>() {

    private val _viewModel: RaceListViewModel
        get() = viewModel as RaceListViewModel

    private lateinit var _binding: FragmentRaceListBinding
    private lateinit var _adapter: RaceListAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRaceListBinding.inflate(inflater)

        val dataSource: RaceDataSource by inject()
        viewModel =
            RaceListViewModel(UserService.currentUser, requireActivity().application, dataSource)

        val clickListener = RaceClickListener {
            _viewModel.onItemSelected(it)
        }
        _adapter = RaceListAdapter(clickListener)

        _binding.list.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = _adapter
        }

        _binding.floatingActionButton.setOnClickListener {
            _viewModel.addRace()
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
                    is RaceListViewModel.Interaction.OpenFilter -> {
                        showFilterDialog(it.worlds)
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

    private fun showFilterDialog(worlds: CollectionListViewModel.FilterData<World>) {

        var selectedWorld = worlds.selected

        val dialog = FilterDialogFragment(onFilter = {
            _viewModel.filter(selectedWorld)
        })

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

    private fun showDetails(race: Race, startEditing: Boolean) {
        findNavController().navigate(RaceListFragmentDirections.actionRacesToDetails(race, startEditing))
        _viewModel.onNavigationCompleted()
    }
}
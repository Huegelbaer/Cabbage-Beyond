package com.cabbagebeyond.ui.collection.forces

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.cabbagebeyond.EmptyListStateModel
import com.cabbagebeyond.FilterDialogFragment
import com.cabbagebeyond.data.ForceDataSource
import com.cabbagebeyond.databinding.FragmentForcesListBinding
import com.cabbagebeyond.model.Force
import com.cabbagebeyond.model.World
import com.cabbagebeyond.services.UserService
import com.cabbagebeyond.ui.collection.CollectionListFragment
import com.cabbagebeyond.ui.collection.CollectionListViewModel
import org.koin.android.ext.android.inject

class ForcesFragment : CollectionListFragment() {

    private val _viewModel: ForcesViewModel
        get() = viewModel as ForcesViewModel

    private lateinit var _binding: FragmentForcesListBinding
    private lateinit var _adapter: ForcesRecyclerViewAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentForcesListBinding.inflate(inflater)

        val dataSource: ForceDataSource by inject()
        viewModel = ForcesViewModel(UserService.currentUser, requireActivity().application, dataSource)

        val clickListener = ForceClickListener {
            _viewModel.onForceClicked(it)
        }
        _adapter = ForcesRecyclerViewAdapter(clickListener)

        _binding.list.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = _adapter
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

        _viewModel.items.observe(viewLifecycleOwner) {
            it?.let {
                _adapter.submitList(it)
            }
        }

        _viewModel.selectedForce.observe(viewLifecycleOwner) {
            it?.let {
                showDetails(it)
            }
        }

        _viewModel.interaction.observe(viewLifecycleOwner) {
            it?.let {
                when (it) {
                    is ForcesViewModel.Interaction.OpenFilter -> {
                        showFilterDialog(it.ranks, it.worlds)
                    }
                }
                _viewModel.onInteractionCompleted()
            }
        }
    }

    override fun showList() {
        _binding.list.visibility = View.VISIBLE
        _binding.emptyStateView.root.visibility = View.GONE
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

    private fun showFilterDialog(ranks: CollectionListViewModel.FilterData<ForceRank>, worlds: CollectionListViewModel.FilterData<World>) {

        var selectedRank = ranks.selected
        var selectedWorld = worlds.selected

        val dialog = FilterDialogFragment(onFilter = {
            _viewModel.filter(selectedRank, selectedWorld)
        })

        dialog.addFilterChipGroup(ranks.title, ranks.values, ranks.selected, ranks.titleProperty) {
            selectedRank = it
        }
        dialog.addFilterChipGroup(worlds.title, worlds.values, worlds.selected, worlds.titleProperty) {
            selectedWorld = it
        }

        dialog.show(requireActivity().supportFragmentManager, "force_dialog_filter")
    }

    private fun showDetails(force: Force) {
        findNavController().navigate(ForcesFragmentDirections.actionForcesToDetails(force))
        _viewModel.onNavigationCompleted()
    }
}
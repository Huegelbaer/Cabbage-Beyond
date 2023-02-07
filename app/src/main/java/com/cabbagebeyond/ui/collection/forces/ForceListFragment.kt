package com.cabbagebeyond.ui.collection.forces

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.cabbagebeyond.EmptyListStateModel
import com.cabbagebeyond.FilterDialogFragment
import com.cabbagebeyond.data.ForceDataSource
import com.cabbagebeyond.databinding.FragmentForceListBinding
import com.cabbagebeyond.model.Force
import com.cabbagebeyond.model.World
import com.cabbagebeyond.services.UserService
import com.cabbagebeyond.ui.collection.CollectionListFragment
import com.cabbagebeyond.ui.collection.CollectionListViewModel
import org.koin.android.ext.android.inject

class ForceListFragment : CollectionListFragment<Force>() {

    private val _viewModel: ForceListViewModel
        get() = viewModel as ForceListViewModel

    private lateinit var _binding: FragmentForceListBinding
    private lateinit var _adapter: ForceListAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentForceListBinding.inflate(inflater)

        val dataSource: ForceDataSource by inject()
        viewModel =
            ForceListViewModel(UserService.currentUser, requireActivity().application, dataSource)

        val clickListener = ForceClickListener {
            _viewModel.onItemSelected(it)
        }
        _adapter = ForceListAdapter(clickListener)

        _binding.list.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = _adapter
        }

        _binding.floatingActionButton.setOnClickListener {
            _viewModel.addForce()
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
                    is ForceListViewModel.Interaction.OpenFilter -> {
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

    private fun showFilterDialog(
        ranks: CollectionListViewModel.FilterData<ForceRank>,
        worlds: CollectionListViewModel.FilterData<World>
    ) {

        var selectedRank = ranks.selected
        var selectedWorld = worlds.selected

        val dialog = FilterDialogFragment(onFilter = {
            _viewModel.filter(selectedRank, selectedWorld)
        })

        dialog.addFilterChipGroup(ranks.title, ranks.values, ranks.selected, ranks.titleProperty) {
            selectedRank = it
        }
        dialog.addFilterChipGroup(
            worlds.title,
            worlds.values,
            worlds.selected,
            worlds.titleProperty
        ) {
            selectedWorld = it
        }

        dialog.show(requireActivity().supportFragmentManager, "force_dialog_filter")
    }

    private fun showDetails(force: Force, startEditing: Boolean) {
        findNavController().navigate(ForceListFragmentDirections.actionForcesToDetails(force, startEditing))
        _viewModel.onNavigationCompleted()
    }
}
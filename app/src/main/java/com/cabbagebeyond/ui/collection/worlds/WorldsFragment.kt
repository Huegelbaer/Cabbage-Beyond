package com.cabbagebeyond.ui.collection.worlds

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.cabbagebeyond.EmptyListStateModel
import com.cabbagebeyond.data.WorldDataSource
import com.cabbagebeyond.databinding.FragmentWorldsListBinding
import com.cabbagebeyond.model.World
import com.cabbagebeyond.services.UserService
import com.cabbagebeyond.ui.collection.CollectionListFragment
import org.koin.android.ext.android.inject


class WorldsFragment : CollectionListFragment() {

    private val _viewModel: WorldsViewModel
        get() = viewModel as WorldsViewModel

    private lateinit var _binding: FragmentWorldsListBinding
    private lateinit var _adapter: WorldRecyclerViewAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentWorldsListBinding.inflate(inflater)

        val dataSource: WorldDataSource by inject()
        viewModel = WorldsViewModel(UserService.currentUser, requireActivity().application, dataSource)

        val clickListener = WorldClickListener {
            _viewModel.onSelectWorld(it)
        }
        _adapter = WorldRecyclerViewAdapter(clickListener)

        _binding.list.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = _adapter
        }

        _binding.floatingActionButton.setOnClickListener { _viewModel.addWorld() }

        setupViewModelObservers()

        return _binding.root
    }

    override fun setupViewModelObservers() {
        super.setupViewModelObservers()

        _viewModel.items.observe(viewLifecycleOwner) { worlds ->
            worlds?.let {
                _adapter.submitList(it)
            }
        }

        _viewModel.selectedWorld.observe(viewLifecycleOwner) { world ->
            world?.let {
                navigateToDetails(it.first, it.second)
            }
        }

        _viewModel.userCanAddNewContent.observe(viewLifecycleOwner) { canAddContent ->
            canAddContent?.let {
                _binding.floatingActionButton.visibility = if (it) View.VISIBLE else View.GONE
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

    private fun navigateToDetails(world: World, startEditing: Boolean) {
        findNavController().navigate(WorldsFragmentDirections.actionHomeToWorldDetails(world, startEditing))
        _viewModel.onNavigationCompleted()
    }
}
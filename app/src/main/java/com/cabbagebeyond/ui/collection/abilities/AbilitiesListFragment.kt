package com.cabbagebeyond.ui.collection.abilities

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.cabbagebeyond.EmptyListStateModel
import com.cabbagebeyond.FilterDialogFragment
import com.cabbagebeyond.data.AbilityDataSource
import com.cabbagebeyond.databinding.FragmentAbilityListBinding
import com.cabbagebeyond.model.Ability
import com.cabbagebeyond.model.World
import com.cabbagebeyond.services.UserService
import com.cabbagebeyond.ui.collection.CollectionListFragment
import com.cabbagebeyond.ui.collection.CollectionListViewModel
import org.koin.android.ext.android.inject

class AbilitiesListFragment : CollectionListFragment<Ability>() {

    private val _viewModel: AbilityListViewModel
        get() = viewModel as AbilityListViewModel

    private lateinit var _binding: FragmentAbilityListBinding
    private lateinit var _adapter: AbilityListAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAbilityListBinding.inflate(inflater)

        val dataSource: AbilityDataSource by inject()
        viewModel =
            AbilityListViewModel(UserService.currentUser, requireActivity().application, dataSource)

        val clickListener = AbilityClickListener {
            _viewModel.onItemSelected(it)
        }
        _adapter = AbilityListAdapter(clickListener)

        _binding.list.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = _adapter
        }

        _binding.floatingActionButton.setOnClickListener {
            _viewModel.addAbility()
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
                    is AbilityListViewModel.Interaction.OpenFilter -> {
                        showFilterDialog(it.attributes, it.worlds)
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
        attributes: CollectionListViewModel.FilterData<AbilityAttribute>,
        worlds: CollectionListViewModel.FilterData<World>
    ) {

        var selectedAttribute = attributes.selected
        var selectedWorld = worlds.selected

        val dialog = FilterDialogFragment(onFilter = {
            _viewModel.filter(selectedAttribute, selectedWorld)
        })

        dialog.addFilterChipGroup(
            attributes.title,
            attributes.values,
            attributes.selected,
            attributes.titleProperty
        ) {
            selectedAttribute = it
        }
        dialog.addFilterChipGroup(
            worlds.title,
            worlds.values,
            worlds.selected,
            worlds.titleProperty
        ) {
            selectedWorld = it
        }

        dialog.show(requireActivity().supportFragmentManager, "ability_dialog_filter")
    }

    private fun showDetails(ability: Ability, startEditing: Boolean) {
        findNavController().navigate(
            AbilitiesListFragmentDirections.actionAbilitiesToDetails(
                ability,
                startEditing
            )
        )
        _viewModel.onNavigationCompleted()
    }
}
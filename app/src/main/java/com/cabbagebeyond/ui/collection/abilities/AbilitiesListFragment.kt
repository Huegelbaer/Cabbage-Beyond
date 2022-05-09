package com.cabbagebeyond.ui.collection.abilities

import android.os.Bundle
import android.view.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.navigation.fragment.findNavController
import com.cabbagebeyond.FilterDialogFragment
import com.cabbagebeyond.data.AbilityDataSource
import com.cabbagebeyond.databinding.FragmentAbilitiesListBinding
import com.cabbagebeyond.model.Ability
import com.cabbagebeyond.model.World
import com.cabbagebeyond.ui.collection.CollectionListFragment
import com.cabbagebeyond.ui.collection.CollectionListViewModel
import org.koin.android.ext.android.inject

class AbilitiesListFragment : CollectionListFragment() {

    private val _viewModel: AbilitiesViewModel
        get() = viewModel as AbilitiesViewModel

    private lateinit var _binding: FragmentAbilitiesListBinding
    private lateinit var _adapter: AbilitiesRecyclerViewAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAbilitiesListBinding.inflate(inflater)

        val dataSource: AbilityDataSource by inject()
        viewModel = AbilitiesViewModel(requireActivity().application, dataSource)

        val clickListener = AbilityClickListener {
            _viewModel.onAbilityClicked(it)
        }
        _adapter = AbilitiesRecyclerViewAdapter(clickListener)

        _binding.list.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = _adapter
        }

        _viewModel.items.observe(viewLifecycleOwner) {
            it?.let {
                _adapter.submitList(it)
            }
        }

        _viewModel.selectedAbility.observe(viewLifecycleOwner) {
            it?.let {
                showDetails(it)
            }
        }

        _viewModel.interaction.observe(viewLifecycleOwner) {
            it?.let {
                when (it) {
                    is AbilitiesViewModel.Interaction.OpenFilter -> {
                        showFilterDialog(it.attributes, it.worlds)
                    }
                }
                _viewModel.onInteractionCompleted()
            }
        }

        setHasOptionsMenu(true)

        return _binding.root
    }

    private fun showFilterDialog(attributes: CollectionListViewModel.FilterData<AbilityAttribute>, worlds: CollectionListViewModel.FilterData<World>) {

        var selectedAttribute = attributes.selected
        var selectedWorld = worlds.selected

        val dialog = FilterDialogFragment(onFilter = {
            _viewModel.filter(selectedAttribute, selectedWorld)
        })

        dialog.addFilterChipGroup(attributes.title, attributes.values, attributes.selected, attributes.titleProperty) {
            selectedAttribute = it
        }
        dialog.addFilterChipGroup(worlds.title, worlds.values, worlds.selected, worlds.titleProperty) {
            selectedWorld = it
        }

        dialog.show(requireActivity().supportFragmentManager, "ability_dialog_filter")
    }

    private fun showDetails(ability: Ability) {
        findNavController().navigate(AbilitiesListFragmentDirections.actionAbilitiesToDetails(ability))
        _viewModel.onNavigationCompleted()
    }
}
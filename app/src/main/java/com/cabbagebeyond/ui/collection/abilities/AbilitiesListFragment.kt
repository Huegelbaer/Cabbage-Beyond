package com.cabbagebeyond.ui.collection.abilities

import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.navigation.fragment.findNavController
import com.cabbagebeyond.R
import com.cabbagebeyond.data.AbilityDataSource
import com.cabbagebeyond.databinding.FragmentAbilitiesListBinding
import com.cabbagebeyond.model.Ability
import com.cabbagebeyond.model.Race
import com.cabbagebeyond.model.World
import com.cabbagebeyond.ui.collection.CollectionListFragment
import com.cabbagebeyond.ui.collection.CollectionListViewModel
import com.cabbagebeyond.ui.collection.abilities.details.AbilityDetailsViewModel
import com.cabbagebeyond.ui.collection.characters.CharacterListViewModel
import com.google.android.material.chip.ChipGroup
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
        val viewInflated = layoutInflater.inflate(R.layout.content_view_abilities_filter, null)
        val attributesChipGroup = viewInflated.findViewById<ChipGroup>(R.id.filter_attribute_chip_group)
        val worldsChipGroup = viewInflated.findViewById<ChipGroup>(R.id.filter_world_chip_group)

        prepareChipGroup(attributesChipGroup, attributes)
        prepareChipGroup(worldsChipGroup, worlds)

        AlertDialog.Builder(requireContext())
            .setTitle(R.string.menu_filter)
            .setView(viewInflated)
            .setPositiveButton(R.string.menu_filter) { _, _ ->
                val selectedAttribute = attributes.values.getOrNull(attributesChipGroup.checkedChipId)
                val selectedWorld = worlds.values.getOrNull(worldsChipGroup.checkedChipId)
                _viewModel.filter(selectedAttribute, selectedWorld)
            }
            .setNegativeButton(R.string.dialog_button_cancel, null)
            .show()
    }

    private fun showDetails(ability: Ability) {
        findNavController().navigate(AbilitiesListFragmentDirections.actionAbilitiesToDetails(ability))
        _viewModel.onNavigationCompleted()
    }
}
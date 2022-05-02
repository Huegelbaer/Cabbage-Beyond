package com.cabbagebeyond.ui.collection.characters

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AlertDialog
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.cabbagebeyond.R
import com.cabbagebeyond.data.CharacterDataSource
import com.cabbagebeyond.databinding.FragmentCharacterListBinding
import com.cabbagebeyond.model.Character
import com.cabbagebeyond.model.Race
import com.cabbagebeyond.model.World
import com.cabbagebeyond.ui.collection.CollectionListFragment
import com.cabbagebeyond.ui.collection.CollectionListViewModel
import com.google.android.material.chip.ChipGroup
import org.koin.android.ext.android.inject

class CharacterListFragment : CollectionListFragment() {

    private val _viewModel: CharacterListViewModel
        get() = viewModel as CharacterListViewModel

    private lateinit var _binding: FragmentCharacterListBinding
    private lateinit var _adapter: CharacterListAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCharacterListBinding.inflate(inflater)

        val dataSource: CharacterDataSource by inject()
        viewModel = CharacterListViewModel(requireActivity().application, dataSource)

        val clickListener = CharacterClickListener {
            _viewModel.onCharacterClicked(it)
        }
        _adapter = CharacterListAdapter(clickListener)

        _binding.list.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = _adapter
        }

        _viewModel.items.observe(viewLifecycleOwner) {
            it?.let {
                _adapter.submitList(it)
            }
        }

        _viewModel.selectedCharacter.observe(viewLifecycleOwner) {
            it?.let {
                showCharacterDetails(it)
            }
        }

        _viewModel.interaction.observe(viewLifecycleOwner) {
            it?.let {
                when (it) {
                    is CharacterListViewModel.Interaction.OpenFilter -> {
                        showFilterDialog(it.races, it.types, it.worlds)
                    }
                }
                _viewModel.onInteractionCompleted()
            }
        }

        setHasOptionsMenu(true)

        return _binding.root
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.app_bar_search -> {
                true
            }
            R.id.app_bar_sort_name -> {
                _viewModel.onSelectSort(CharacterListViewModel.SortType.NAME)
                true
            }
            R.id.app_bar_sort_race -> {
                _viewModel.onSelectSort(CharacterListViewModel.SortType.RACE)
                true
            }
            R.id.app_bar_sort_type -> {
                _viewModel.onSelectSort(CharacterListViewModel.SortType.TYPE)
                true
            }
            R.id.app_bar_sort_world -> {
                _viewModel.onSelectSort(CharacterListViewModel.SortType.WORLD)
                true
            }
            R.id.app_bar_filter_list -> {
                _viewModel.onSelectFilter()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    @SuppressLint("ResourceAsColor")
    private fun showFilterDialog(races: CollectionListViewModel.FilterData<Race>, types: CollectionListViewModel.FilterData<CharacterListViewModel.CharacterType>, worlds: CollectionListViewModel.FilterData<World>) {
        val viewInflated = layoutInflater.inflate(R.layout.content_view_filter_characters, null)
        val racesChipGroup = viewInflated.findViewById<ChipGroup>(R.id.filter_race_chip_group)
        val typesChipGroup = viewInflated.findViewById<ChipGroup>(R.id.filter_type_chip_group)
        val worldsChipGroup = viewInflated.findViewById<ChipGroup>(R.id.filter_world_chip_group)

        prepareChipGroup(racesChipGroup, races)
        prepareChipGroup(typesChipGroup, types)
        prepareChipGroup(worldsChipGroup, worlds)

        AlertDialog.Builder(requireContext())
            .setTitle(R.string.menu_filter)
            .setView(viewInflated)
            .setPositiveButton(R.string.menu_filter) { _, _ ->
                val selectedRace = races.values.getOrNull(racesChipGroup.checkedChipId)
                val selectedType = types.values.getOrNull(typesChipGroup.checkedChipId)
                val selectedWorld = worlds.values.getOrNull(worldsChipGroup.checkedChipId)
                _viewModel.filter(selectedRace, selectedType, selectedWorld)
            }
            .setNegativeButton(R.string.dialog_button_cancel, null)
            .show()
    }

    private fun showCharacterDetails(character: Character) {
        findNavController().navigate(CharacterListFragmentDirections.actionCharactersListToDetails(character))
        _viewModel.onNavigationCompleted()
    }
}
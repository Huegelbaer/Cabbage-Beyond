package com.cabbagebeyond.ui.collection.characters

import android.annotation.SuppressLint
import android.app.SearchManager
import android.content.Context
import android.content.res.ColorStateList
import android.os.Bundle
import android.view.*
import android.widget.SearchView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.cabbagebeyond.R
import com.cabbagebeyond.data.CharacterDataSource
import com.cabbagebeyond.databinding.FragmentCharacterListBinding
import com.cabbagebeyond.model.Character
import com.cabbagebeyond.model.Race
import com.cabbagebeyond.model.World
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import org.koin.android.ext.android.inject

class CharacterListFragment : Fragment() {

    private val _viewModel: CharacterListViewModel by lazy {
        val dataSource: CharacterDataSource by inject()
        CharacterListViewModel(requireActivity().application, dataSource)
    }

    private lateinit var _binding: FragmentCharacterListBinding
    private lateinit var _adapter: CharacterListAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCharacterListBinding.inflate(inflater)

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

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)

        inflater.inflate(R.menu.character_list, menu)

        // Associate searchable configuration with the SearchView
        val searchManager = requireActivity().getSystemService(Context.SEARCH_SERVICE) as SearchManager

        val queryTextListener: SearchView.OnQueryTextListener =
            object : SearchView.OnQueryTextListener {
                override fun onQueryTextChange(newText: String): Boolean {
                  //  val textView = findViewById(R.id.aa) as TextView
                    //textView.text = newText
                    if (newText.isEmpty()) {
                        _viewModel.onSearchCanceled()
                    }
                    return true
                }

                override fun onQueryTextSubmit(query: String): Boolean {
                    _viewModel.onSearchCharacter(query)
                    return true
                }
            }
        (menu.findItem(R.id.app_bar_search).actionView as SearchView).apply {
            setSearchableInfo(searchManager.getSearchableInfo(requireActivity().componentName))
            setOnQueryTextListener(queryTextListener)
        }
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
    private fun showFilterDialog(races: CharacterListViewModel.FilterData<Race>, types: CharacterListViewModel.FilterData<CharacterListViewModel.CharacterType>, worlds: CharacterListViewModel.FilterData<World>) {
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

    private fun <T : Any> prepareChipGroup(chipGroup: ChipGroup, data: CharacterListViewModel.FilterData<T>) {
        data.values.forEachIndexed { index, it ->
            val title = data.title.get(it)
            val chip = createChip(title, index)
            chipGroup.addView(chip)
        }
        data.selected?.let {
            val index = data.values.indexOf(it)
            chipGroup.check(index)
        }
    }

    private fun createChip(title: String, index: Int): Chip {
        return Chip(context).apply {
            id = index
            tag = index
            text = title
            isClickable = true
            isCheckable = true
            isCheckedIconVisible = true
            isFocusable = true
            chipBackgroundColor = ColorStateList.valueOf(resources.getColor(R.color.selector_chip_background))
        }
    }

    private fun showCharacterDetails(character: Character) {
        findNavController().navigate(CharacterListFragmentDirections.actionCharactersListToDetails(character))
        _viewModel.onNavigationCompleted()
    }
}
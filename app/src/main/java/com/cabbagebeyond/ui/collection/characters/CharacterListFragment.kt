package com.cabbagebeyond.ui.collection.characters

import android.os.Bundle
import android.view.*
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.cabbagebeyond.EmptyListStateModel
import com.cabbagebeyond.FilterDialogFragment
import com.cabbagebeyond.R
import com.cabbagebeyond.data.CharacterDataSource
import com.cabbagebeyond.databinding.FragmentCharacterListBinding
import com.cabbagebeyond.model.Character
import com.cabbagebeyond.model.Race
import com.cabbagebeyond.model.World
import com.cabbagebeyond.services.UserService
import com.cabbagebeyond.ui.collection.CollectionListFragment
import com.cabbagebeyond.ui.collection.CollectionListViewModel
import org.koin.android.ext.android.inject

class CharacterListFragment : CollectionListFragment<Character>() {

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
        viewModel = CharacterListViewModel(
            UserService.currentUser,
            requireActivity().application,
            dataSource
        )

        val clickListener = CharacterClickListener {
            _viewModel.onItemSelected(it)
        }
        _adapter = CharacterListAdapter(clickListener)

        _binding.list.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = _adapter
        }

        _binding.floatingActionButton.setOnClickListener {
            _viewModel.addCharacter()
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

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.collection_characters_sort, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
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
            else -> super.onOptionsItemSelected(item)
        }
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
                showCharacterDetails(it.first, it.second)
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
        races: CollectionListViewModel.FilterData<Race>,
        types: CollectionListViewModel.FilterData<CharacterType>,
        worlds: CollectionListViewModel.FilterData<World>
    ) {

        var selectedRace = races.selected
        var selectedType = types.selected
        var selectedWorld = worlds.selected

        val dialog = FilterDialogFragment(onFilter = {
            _viewModel.filter(selectedRace, selectedType, selectedWorld)
        })

        dialog.addFilterChipGroup(races.title, races.values, races.selected, races.titleProperty) {
            selectedRace = it
        }
        dialog.addFilterChipGroup(types.title, types.values, types.selected, types.titleProperty) {
            selectedType = it
        }
        dialog.addFilterChipGroup(
            worlds.title,
            worlds.values,
            worlds.selected,
            worlds.titleProperty
        ) {
            selectedWorld = it
        }

        dialog.show(requireActivity().supportFragmentManager, "character_dialog_filter")
    }

    private fun showCharacterDetails(character: Character, startEditing: Boolean) {
        findNavController().navigate(
            CharacterListFragmentDirections.actionCharactersListToDetails(
                character,
                startEditing
            )
        )
        _viewModel.onNavigationCompleted()
    }
}
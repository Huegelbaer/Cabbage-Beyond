package com.cabbagebeyond.ui.collection.characters

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.cabbagebeyond.R
import com.cabbagebeyond.data.Database
import com.cabbagebeyond.data.repository.CharacterRepository
import com.cabbagebeyond.databinding.FragmentCharacterListBinding
import com.cabbagebeyond.model.Character

/**
 * A fragment representing a list of Items.
 */
class CharacterListFragment : Fragment() {

    private val _viewModel: CharacterListViewModel by lazy {
        val repository = CharacterRepository(Database.characterDao)
        CharacterListViewModel(repository)
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

        _viewModel.items.observe(viewLifecycleOwner, Observer {
            it?.let {
                _adapter.submitList(it)
            }
        })

        _viewModel.selectedCharacter.observe(viewLifecycleOwner, Observer {
            it?.let {
                showCharacterDetails(it)
            }
        })

        setHasOptionsMenu(true)

        return _binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)

        inflater.inflate(R.menu.character_list, menu)
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
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun showCharacterDetails(character: Character) {
        findNavController().navigate(CharacterListFragmentDirections.actionCharactersListToDetails(character))
        _viewModel.onNavigationCompleted()
    }
}
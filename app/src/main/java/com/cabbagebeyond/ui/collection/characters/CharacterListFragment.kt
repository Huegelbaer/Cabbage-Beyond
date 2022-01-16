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

    private var columnCount = 1

    private val _viewModel: CharacterListViewModel by lazy {
        val repository = CharacterRepository(Database.characterDao)
        CharacterListViewModel(repository)
    }
    private lateinit var _binding: FragmentCharacterListBinding
    private lateinit var _adapter: CharacterListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            columnCount = it.getInt(ARG_COLUMN_COUNT)
        }
    }

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

        return _binding.root
    }

    companion object {

        // TODO: Customize parameter argument names
        const val ARG_COLUMN_COUNT = "column-count"

        // TODO: Customize parameter initialization
        @JvmStatic
        fun newInstance(columnCount: Int) =
            CharacterListFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_COLUMN_COUNT, columnCount)
                }
            }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)

        inflater.inflate(R.menu.character_list, menu)
    }

    private fun showCharacterDetails(character: Character) {
        findNavController().navigate(CharacterListFragmentDirections.actionCharactersListToDetails(character))
        _viewModel.onNavigationCompleted()
    }
}
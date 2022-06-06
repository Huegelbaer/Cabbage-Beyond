package com.cabbagebeyond.ui.account

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.ArrayAdapter
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.cabbagebeyond.R
import com.cabbagebeyond.data.CharacterDataSource
import com.cabbagebeyond.data.UserDataSource
import com.cabbagebeyond.data.WorldDataSource
import com.cabbagebeyond.databinding.FragmentAccountBinding
import com.cabbagebeyond.model.Character
import com.cabbagebeyond.ui.CharacterAdapter
import com.cabbagebeyond.ui.CharacterClickListener
import org.koin.android.ext.android.inject

class AccountFragment : Fragment() {

    private val viewModel: AccountViewModel by lazy {
        val userDataSource: UserDataSource by inject()
        val characterDataSource: CharacterDataSource by inject()
        val worldDataSource: WorldDataSource by inject()
        AccountViewModel(userDataSource, characterDataSource, worldDataSource)
    }

    private lateinit var _binding: FragmentAccountBinding
    private lateinit var _adapter: CharacterAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAccountBinding.inflate(inflater)

        viewModel.account.observe(viewLifecycleOwner) { account ->
            _binding.account = account

            val worlds = viewModel.worlds.value ?: listOf()
            setupSpinner(worlds.map { it.name }, account.activeWorld)
            setupRecyclerView(account.characters)
        }

        setHasOptionsMenu(true)

        return _binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.account, menu)
    }

    private fun setupSpinner(worlds: List<String>, activeWorld: String) {
        ArrayAdapter(
            requireContext(),
            R.layout.spinner_item,
            worlds
        ).also { adapter ->
            adapter.setDropDownViewResource(R.layout.spinner_dropdown_item)
            _binding.spinnerWorld.adapter = adapter
        }
        val position = worlds.indexOf(activeWorld)
        _binding.spinnerWorld.setSelection(position)
    }

    private fun setupRecyclerView(characters: List<Character>) {
        val clickListener = CharacterClickListener {
            showCharacter(it)
        }
        _adapter = CharacterAdapter(clickListener)

        _binding.ownCharactersList.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = _adapter
        }

        _adapter.submitList(characters)
    }

    private fun showCharacter(character: Character) {
        findNavController().navigate(AccountFragmentDirections.actionAccountToCharacterDetails(character))
    }
}
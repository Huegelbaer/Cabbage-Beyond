package com.cabbagebeyond.ui.account

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.cabbagebeyond.databinding.FragmentAccountBinding
import com.cabbagebeyond.model.Character
import com.cabbagebeyond.ui.CharacterAdapter
import com.cabbagebeyond.ui.CharacterClickListener

class AccountFragment : Fragment() {

    private val viewModel: AccountViewModel by activityViewModels()

    private lateinit var _binding: FragmentAccountBinding
    private lateinit var _adapter: CharacterAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAccountBinding.inflate(inflater)

        viewModel.account.observe(viewLifecycleOwner, Observer { account ->
            _binding.account = account

            val worlds = viewModel.worlds.value ?: listOf()
            setupSpinner(worlds.map { it.name }, account.activeWorld)
            setupRecyclerView(account.characters)
        })

        return _binding.root
    }

    private fun setupSpinner(worlds: List<String>, activeWorld: String) {
        ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item,
            worlds
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            _binding.spinnerWorld.adapter = adapter
        }
        val position = worlds.indexOf(activeWorld)
        _binding.spinnerWorld.setSelection(position)
    }

    private fun setupRecyclerView(characters: List<Character>) {
        val clickListener = CharacterClickListener {

        }
        _adapter = CharacterAdapter(clickListener)

        _binding.ownCharactersList.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = _adapter
        }

        _adapter.submitList(characters)
    }
}
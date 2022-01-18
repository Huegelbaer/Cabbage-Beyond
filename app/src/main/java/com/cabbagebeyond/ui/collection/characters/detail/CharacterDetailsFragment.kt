package com.cabbagebeyond.ui.collection.characters.detail

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.cabbagebeyond.data.CharacterDataSource
import com.cabbagebeyond.data.Database
import com.cabbagebeyond.data.repository.*
import com.cabbagebeyond.databinding.CharacterDetailsFragmentBinding
import org.koin.android.ext.android.inject

class CharacterDetailsFragment : Fragment() {

    companion object {
        fun newInstance() = CharacterDetailsFragment()
    }

    private lateinit var viewModel: CharacterDetailsViewModel
    private lateinit var _adapter: CharacterDetailsAdapter
    private lateinit var _binding: CharacterDetailsFragmentBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = CharacterDetailsFragmentBinding.inflate(inflater)

        val character = CharacterDetailsFragmentArgs.fromBundle(requireArguments()).character
        _binding.character = character

        _adapter = CharacterDetailsAdapter({
            viewModel.expandHeader(it)
        }, {
            viewModel.collapseHeader(it)
        }, {
            viewModel.show(it)
        })
        _binding.list.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = _adapter
        }

        viewModel = CharacterDetailsViewModel(character)

        viewModel.items.observe(viewLifecycleOwner, Observer {
            it?.let {
                _adapter.submitList(ArrayList(it))
            }
        })

        return _binding.root
    }

}
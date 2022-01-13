package com.cabbagebeyond.ui.collection.characters.detail

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.cabbagebeyond.databinding.CharacterDetailsFragmentBinding

class CharacterDetailsFragment : Fragment() {

    companion object {
        fun newInstance() = CharacterDetailsFragment()
    }

    private lateinit var viewModel: CharacterDetailsViewModel
    private lateinit var _binding: CharacterDetailsFragmentBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = CharacterDetailsFragmentBinding.inflate(inflater)

        val character = CharacterDetailsFragmentArgs.fromBundle(requireArguments()).character
        _binding.character = character

        viewModel = CharacterDetailsViewModel(character)

        return _binding.root
    }

}
package com.cabbagebeyond.ui.collection.characters.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.cabbagebeyond.data.CharacterDataSource
import com.cabbagebeyond.databinding.CharacterDetailsFragmentBinding
import com.cabbagebeyond.services.UserService
import com.cabbagebeyond.ui.DetailsFragment
import org.koin.android.ext.android.inject

class CharacterDetailsFragment : DetailsFragment() {

    companion object {
        fun newInstance() = CharacterDetailsFragment()
    }

    private lateinit var _viewModel: CharacterDetailsViewModel
    private lateinit var _adapter: CharacterDetailsAdapter
    private lateinit var _binding: CharacterDetailsFragmentBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = CharacterDetailsFragmentBinding.inflate(inflater)

        val character = CharacterDetailsFragmentArgs.fromBundle(requireArguments()).character

        val dataSource: CharacterDataSource by inject()
        _viewModel = CharacterDetailsViewModel(
            character, dataSource,UserService.currentUser, requireActivity().application
        )

        _binding.viewModel = _viewModel
        _binding.lifecycleOwner = this

        _adapter = CharacterDetailsAdapter({
            _viewModel.expandHeader(it)
        }, {
            _viewModel.collapseHeader(it)
        }, {
            _viewModel.show(it)
        })
        _binding.readGroup.list.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = _adapter
        }

        _viewModel.items.observe(viewLifecycleOwner) {
            it?.let {
                _adapter.submitList(ArrayList(it))
            }
        }

        _viewModel.fabImage.observe(viewLifecycleOwner) {
            it?.let {
                _binding.floatingActionButton.setImageResource(it)
            }
        }

        _viewModel.isEditing.observe(viewLifecycleOwner) {
            it?.let { isEditing ->
                toggleVisibility(_binding.readGroup.root, _binding.editGroup.root, isEditing)
            }
        }

        _viewModel.message.observe(viewLifecycleOwner) {
            it?.let {
                showSnackbar(resources.getString(it))
            }
        }

        return _binding.root
    }

}

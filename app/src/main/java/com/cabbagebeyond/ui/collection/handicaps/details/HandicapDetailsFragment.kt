package com.cabbagebeyond.ui.collection.handicaps.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.cabbagebeyond.data.HandicapDataSource
import com.cabbagebeyond.data.WorldDataSource
import com.cabbagebeyond.databinding.FragmentHandicapDetailsBinding
import com.cabbagebeyond.model.World
import com.cabbagebeyond.services.UserService
import com.cabbagebeyond.ui.DetailsFragment
import org.koin.android.ext.android.inject

class HandicapDetailsFragment : DetailsFragment() {

    companion object {
        fun newInstance() = HandicapDetailsFragment()
    }

    private val _viewModel: HandicapDetailsViewModel
        get() = viewModel as HandicapDetailsViewModel

    private lateinit var _binding: FragmentHandicapDetailsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentHandicapDetailsBinding.inflate(inflater)

        val handicap = HandicapDetailsFragmentArgs.fromBundle(requireArguments()).handicap

        val dataSource: HandicapDataSource by inject()
        val worldDataSource: WorldDataSource by inject()
        viewModel = HandicapDetailsViewModel(
            handicap, dataSource, worldDataSource, UserService.currentUser, requireActivity().application
        )

        _binding.viewModel = _viewModel
        _binding.lifecycleOwner = this

        _viewModel.fabImage.observe(viewLifecycleOwner) {
            it?.let {
                _binding.floatingActionButton.setImageResource(it)
            }
        }

        _viewModel.isEditing.observe(viewLifecycleOwner) {
            it?.let { isEditing ->
                toggleVisibility(_binding.readGroup, _binding.editGroup, isEditing)
            }
        }

        _viewModel.types.observe(viewLifecycleOwner) {
            setupTypesSpinner(handicap.type, it ?: listOf())
        }

        _viewModel.worlds.observe(viewLifecycleOwner) {
            setupWorldSpinner(handicap.world, it ?: listOf())
        }

        _viewModel.message.observe(viewLifecycleOwner) {
            it?.let {
                showSnackbar(resources.getString(it))
            }
        }

        setHasOptionsMenu(true)

        return _binding.root
    }

    private fun setupTypesSpinner(type: String, types: List<String>) {
        setupStringSpinner(type, types, _binding.typeSpinner) {
            _viewModel.onTypeSelected(it)
        }
    }

    private fun setupWorldSpinner(world: World?, worlds: List<World?>) {
        super.setupWorldSpinner(world, worlds, _binding.worldSpinner) {
            _viewModel.onWorldSelected(it)
        }
    }


    override fun navigateToOcr() {
        findNavController().navigate(HandicapDetailsFragmentDirections.actionHandicapDetailsToOcr(_viewModel.properties))
    }
}


package com.cabbagebeyond.ui.collection.worlds.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.cabbagebeyond.data.WorldDataSource
import com.cabbagebeyond.databinding.FragmentWorldDetailsBinding
import com.cabbagebeyond.services.UserService
import com.cabbagebeyond.ui.DetailsFragment
import com.cabbagebeyond.ui.collection.abilities.details.AbilityDetailsViewModel
import org.koin.android.ext.android.inject

class WorldDetailsFragment : DetailsFragment() {

    private val _viewModel: WorldDetailsViewModel
        get() = viewModel as WorldDetailsViewModel

    private lateinit var _binding: FragmentWorldDetailsBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentWorldDetailsBinding.inflate(inflater)

        val world = WorldDetailsFragmentArgs.fromBundle(requireArguments()).world

        val dataSource: WorldDataSource by inject()
        viewModel = WorldDetailsViewModel(
            world, dataSource, UserService.currentUser, requireActivity().application
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

        _viewModel.message.observe(viewLifecycleOwner) {
            it?.let {
                showSnackbar(resources.getString(it))
            }
        }

        setHasOptionsMenu(true)

        return _binding.root
    }

    override fun navigateToOcr() {
        findNavController().navigate(WorldDetailsFragmentDirections.actionWorldDetailsToOcr(_viewModel.properties))
    }
}
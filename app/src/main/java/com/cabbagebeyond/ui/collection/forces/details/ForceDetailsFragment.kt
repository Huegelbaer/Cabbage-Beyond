package com.cabbagebeyond.ui.collection.forces.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.cabbagebeyond.data.ForceDataSource
import com.cabbagebeyond.data.WorldDataSource
import com.cabbagebeyond.databinding.ForceDetailsFragmentBinding
import com.cabbagebeyond.model.World
import com.cabbagebeyond.services.UserService
import com.cabbagebeyond.ui.DetailsFragment
import org.koin.android.ext.android.inject

class ForceDetailsFragment : DetailsFragment() {

    companion object {
        fun newInstance() = ForceDetailsFragment()
    }

    private val _viewModel: ForceDetailsViewModel
        get() = viewModel as ForceDetailsViewModel

    private lateinit var _binding: ForceDetailsFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = ForceDetailsFragmentBinding.inflate(inflater)

        val force = ForceDetailsFragmentArgs.fromBundle(requireArguments()).force

        val dataSource: ForceDataSource by inject()
        val worldDataSource: WorldDataSource by inject()
        viewModel = ForceDetailsViewModel(
            force, dataSource, worldDataSource, UserService.currentUser, requireActivity().application
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

        _viewModel.ranks.observe(viewLifecycleOwner) {
            setupRankSpinner(force.rangRequirement, it ?: listOf())
        }

        _viewModel.worlds.observe(viewLifecycleOwner) {
            setupWorldSpinner(force.world, it ?: listOf())
        }

        _viewModel.message.observe(viewLifecycleOwner) {
            it?.let {
                showSnackbar(resources.getString(it))
            }
        }

        setHasOptionsMenu(true)

        return _binding.root
    }

    private fun setupRankSpinner(rank: String, ranks: List<String>) {
        setupStringSpinner(rank, ranks, _binding.requirementSpinner) {
            _viewModel.onRankSelected(it)
        }
    }

    private fun setupWorldSpinner(world: World?, worlds: List<World?>) {
        super.setupWorldSpinner(world, worlds, _binding.worldSpinner) {
            _viewModel.onWorldSelected(it)
        }
    }

    override fun navigateToOcr() {
        findNavController().navigate(ForceDetailsFragmentDirections.actionForceDetailsToOcr(_viewModel.properties))
    }
}


package com.cabbagebeyond.ui.collection.forces.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.cabbagebeyond.data.ForceDataSource
import com.cabbagebeyond.data.WorldDataSource
import com.cabbagebeyond.databinding.FragmentForceDetailsBinding
import com.cabbagebeyond.model.World
import com.cabbagebeyond.services.UserService
import com.cabbagebeyond.ui.DetailsFragment
import com.cabbagebeyond.ui.collection.forces.ForceRank
import org.koin.android.ext.android.inject

class ForceDetailsFragment : DetailsFragment() {

    companion object {
        fun newInstance() = ForceDetailsFragment()
    }

    private val _viewModel: ForceDetailsViewModel
        get() = viewModel as ForceDetailsViewModel

    private lateinit var _binding: FragmentForceDetailsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentForceDetailsBinding.inflate(inflater)

        val force = ForceDetailsFragmentArgs.fromBundle(requireArguments()).force
        val isEditingActive = ForceDetailsFragmentArgs.fromBundle(requireArguments()).startEditing

        val dataSource: ForceDataSource by inject()
        val worldDataSource: WorldDataSource by inject()
        viewModel = ForceDetailsViewModel(
            force, isEditingActive, dataSource, worldDataSource, UserService.currentUser, requireActivity().application
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
            setupRankSpinner(it.selected, it.values)
        }

        _viewModel.worlds.observe(viewLifecycleOwner) {
            setupWorldSpinner(it.selected, it.values)
        }

        _viewModel.message.observe(viewLifecycleOwner) {
            it?.let {
                showSnackbar(resources.getString(it))
            }
        }

        setHasOptionsMenu(true)

        return _binding.root
    }

    private fun setupRankSpinner(preSelection: ForceRank?, ranks: List<ForceRank>) {
        if (ranks.isNullOrEmpty()) return

        setupSpinner(
            preSelection?.title,
            ranks.map { it.title },
            _binding.requirementSpinner
        ) { index ->
            _viewModel.onRankSelected(ranks[index])
        }
    }

    private fun setupWorldSpinner(world: World?, worlds: List<World?>) {
        setupSpinner(world?.name ?: "", worlds.mapNotNull { it?.name }, _binding.worldSpinner) {
            _viewModel.onWorldSelected(worlds[it])
        }
    }

    override fun navigateToOcr() {
        findNavController().navigate(ForceDetailsFragmentDirections.actionForceDetailsToOcr(_viewModel.properties))
    }
}


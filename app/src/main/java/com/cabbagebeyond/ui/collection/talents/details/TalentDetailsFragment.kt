package com.cabbagebeyond.ui.collection.talents.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.cabbagebeyond.data.TalentDataSource
import com.cabbagebeyond.data.WorldDataSource
import com.cabbagebeyond.databinding.FragmentTalentDetailsBinding
import com.cabbagebeyond.model.World
import com.cabbagebeyond.services.UserService
import com.cabbagebeyond.ui.DetailsFragment
import org.koin.android.ext.android.inject

class TalentDetailsFragment : DetailsFragment() {

    companion object {
        fun newInstance() = TalentDetailsFragment()
    }


    private val _viewModel: TalentDetailsViewModel
        get() = viewModel as TalentDetailsViewModel

    private lateinit var _binding: FragmentTalentDetailsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentTalentDetailsBinding.inflate(inflater)

        val talent = TalentDetailsFragmentArgs.fromBundle(requireArguments()).talent

        val dataSource: TalentDataSource by inject()
        val worldDataSource: WorldDataSource by inject()
        viewModel = TalentDetailsViewModel(
            talent, dataSource, worldDataSource, UserService.currentUser, requireActivity().application
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
            setupRankSpinner(talent.rangRequirement, it ?: listOf())
        }

        _viewModel.types.observe(viewLifecycleOwner) {
            setupTypeSpinner(talent.type, it ?: listOf())
        }

        _viewModel.worlds.observe(viewLifecycleOwner) {
            setupWorldSpinner(talent.world, it ?: listOf())
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
        setupSpinner(rank, ranks, _binding.requirementSpinner) {
            _viewModel.onRankSelected(ranks[it])
        }
    }

    private fun setupTypeSpinner(type: String, types: List<String>) {
        setupSpinner(type, types, _binding.typeSpinner) {
            _viewModel.onTypeSelected(types[it])
        }
    }

    private fun setupWorldSpinner(world: World?, worlds: List<World?>) {
        setupSpinner(world?.name ?: "", worlds.mapNotNull { it?.name }, _binding.worldSpinner) {
            _viewModel.onWorldSelected(worlds[it])
        }
    }

    override fun navigateToOcr() {
        findNavController().navigate(TalentDetailsFragmentDirections.actionTalentDetailsToOcr(_viewModel.properties))
    }
}

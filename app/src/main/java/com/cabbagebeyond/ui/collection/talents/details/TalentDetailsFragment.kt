package com.cabbagebeyond.ui.collection.talents.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.cabbagebeyond.data.TalentDataSource
import com.cabbagebeyond.data.WorldDataSource
import com.cabbagebeyond.databinding.TalentDetailsFragmentBinding
import com.cabbagebeyond.model.User
import com.cabbagebeyond.model.World
import com.cabbagebeyond.ui.DetailsFragment
import com.cabbagebeyond.util.Feature
import org.koin.android.ext.android.inject

class TalentDetailsFragment : DetailsFragment() {

    companion object {
        fun newInstance() = TalentDetailsFragment()
    }

    private lateinit var _viewModel: TalentDetailsViewModel

    private lateinit var _binding: TalentDetailsFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = TalentDetailsFragmentBinding.inflate(inflater)

        val talent = TalentDetailsFragmentArgs.fromBundle(requireArguments()).talent

        val dataSource: TalentDataSource by inject()
        val worldDataSource: WorldDataSource by inject()
        _viewModel = TalentDetailsViewModel(
            talent, dataSource, worldDataSource, User(
                "", "", listOf(
                    Feature.CONFIGURE_APP.name
                ), listOf(), ""
            ), requireActivity().application
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

        return _binding.root
    }

    private fun setupRankSpinner(rank: String, ranks: List<String>) {
        setupStringSpinner(rank, ranks, _binding.requirementSpinner) {
            _viewModel.onRankSelected(it)
        }
    }

    private fun setupTypeSpinner(type: String, types: List<String>) {
        setupStringSpinner(type, types, _binding.typeSpinner) {
            _viewModel.onTypeSelected(it)
        }
    }

    private fun setupWorldSpinner(world: World?, worlds: List<World?>) {
        super.setupWorldSpinner(world, worlds, _binding.worldSpinner) {
            _viewModel.onWorldSelected(it)
        }
    }

}

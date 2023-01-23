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
import com.cabbagebeyond.ui.collection.talents.TalentRank
import com.cabbagebeyond.ui.collection.talents.TalentType
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
            talent, false, dataSource, worldDataSource, UserService.currentUser, requireActivity().application
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

        _viewModel.types.observe(viewLifecycleOwner) {
            setupTypeSpinner(it.selected, it.values)
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

    private fun setupRankSpinner(preSelection: TalentRank?, ranks: List<TalentRank>) {
        if (ranks.isNullOrEmpty()) return

        setupSpinner(
            preSelection?.title,
            ranks.map { it.title },
            _binding.requirementSpinner
        ) { index ->
            _viewModel.onRankSelected(ranks[index])
        }
    }

    private fun setupTypeSpinner(preSelection: TalentType?, types: List<TalentType>) {
        if (types.isNullOrEmpty()) return

        setupSpinner(
            preSelection?.title,
            types.map { it.title },
            _binding.typeSpinner
        ) { index ->
            _viewModel.onTypeSelected(types[index])
        }
    }

    private fun setupWorldSpinner(preSelection: World?, worlds: List<World?>) {
        if (worlds.isNullOrEmpty()) return

        setupSpinner(
            preSelection?.name,
            worlds.map { it?.name ?: "" },
            _binding.worldSpinner
        ) { index ->
            _viewModel.onWorldSelected(worlds[index])
        }
    }

    override fun navigateToOcr() {
        findNavController().navigate(TalentDetailsFragmentDirections.actionTalentDetailsToOcr(_viewModel.properties))
    }
}

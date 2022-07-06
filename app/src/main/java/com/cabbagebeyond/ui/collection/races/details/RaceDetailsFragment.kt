package com.cabbagebeyond.ui.collection.races.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.cabbagebeyond.data.RaceDataSource
import com.cabbagebeyond.data.WorldDataSource
import com.cabbagebeyond.databinding.FragmentRaceDetailsBinding
import com.cabbagebeyond.model.World
import com.cabbagebeyond.services.UserService
import com.cabbagebeyond.ui.DetailsFragment
import org.koin.android.ext.android.inject

class RaceDetailsFragment : DetailsFragment() {

    companion object {
        fun newInstance() = RaceDetailsFragment()
    }

    private val _viewModel: RaceDetailsViewModel
        get() = viewModel as RaceDetailsViewModel

    private lateinit var _binding: FragmentRaceDetailsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentRaceDetailsBinding.inflate(inflater)

        val race = RaceDetailsFragmentArgs.fromBundle(requireArguments()).race

        val dataSource: RaceDataSource by inject()
        val worldDataSource: WorldDataSource by inject()
        viewModel = RaceDetailsViewModel(
            race, false, dataSource, worldDataSource, UserService.currentUser, requireActivity().application
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

        _viewModel.worlds.observe(viewLifecycleOwner) {
            setupWorldSpinner(race.world, it ?: listOf())
        }

        _viewModel.message.observe(viewLifecycleOwner) {
            it?.let {
                showSnackbar(resources.getString(it))
            }
        }

        setHasOptionsMenu(true)

        return _binding.root
    }

    private fun setupWorldSpinner(world: World?, worlds: List<World?>) {
        setupSpinner(world?.name ?: "", worlds.mapNotNull { it?.name }, _binding.worldSpinner) {
            _viewModel.onWorldSelected(worlds[it])
        }
    }

    override fun navigateToOcr() {
        findNavController().navigate(RaceDetailsFragmentDirections.actionRaceDetailsToOcr(_viewModel.properties))
    }
}


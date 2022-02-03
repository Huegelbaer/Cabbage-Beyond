package com.cabbagebeyond.ui.collection.abilities.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.cabbagebeyond.data.AbilityDataSource
import com.cabbagebeyond.data.WorldDataSource
import com.cabbagebeyond.databinding.AbilityDetailsFragmentBinding
import com.cabbagebeyond.model.World
import com.cabbagebeyond.services.UserService
import com.cabbagebeyond.ui.DetailsFragment
import org.koin.android.ext.android.inject

class AbilityDetailsFragment : DetailsFragment() {

    companion object {
        fun newInstance() = AbilityDetailsFragment()
    }

    private lateinit var _viewModel: AbilityDetailsViewModel

    private lateinit var _binding: AbilityDetailsFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = AbilityDetailsFragmentBinding.inflate(inflater)

        val ability = AbilityDetailsFragmentArgs.fromBundle(requireArguments()).ability

        val dataSource: AbilityDataSource by inject()
        val worldDataSource: WorldDataSource by inject()
        _viewModel = AbilityDetailsViewModel(ability, dataSource, worldDataSource, UserService.currentUser!!, requireActivity().application)

        _binding.viewModel = _viewModel
        _binding.ability = _viewModel.ability.value

        _viewModel.ability.observe(viewLifecycleOwner) {
            it?.let {
                _binding.ability = it
            }
        }

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

        _viewModel.attributes.observe(viewLifecycleOwner) {
            setupAttributeSpinner(ability.attribute, it ?: listOf())
        }

        _viewModel.worlds.observe(viewLifecycleOwner) {
            setupWorldSpinner(ability.world, it ?: listOf())
        }

        _viewModel.message.observe(viewLifecycleOwner) {
            it?.let {
                showSnackbar(resources.getString(it))
            }
        }

        setHasOptionsMenu(true)

        return _binding.root
    }

    private fun setupAttributeSpinner(attribute: String, attributes: List<String>) {
        setupStringSpinner(attribute, attributes, _binding.attributeSpinner) {
            _viewModel.onAttributeSelected(it)
        }
    }

    private fun setupWorldSpinner(world: World?, worlds: List<World?>) {
        super.setupWorldSpinner(world, worlds, _binding.worldSpinner) {
            _viewModel.onWorldSelected(it)
        }
    }

    override fun navigateToOcr() {
        findNavController().navigate(AbilityDetailsFragmentDirections.actionAbilityDetailsToOcr())
    }
}
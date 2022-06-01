package com.cabbagebeyond.ui.collection.abilities.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.cabbagebeyond.data.AbilityDataSource
import com.cabbagebeyond.data.WorldDataSource
import com.cabbagebeyond.databinding.FragmentAbilityDetailsBinding
import com.cabbagebeyond.model.World
import com.cabbagebeyond.services.UserService
import com.cabbagebeyond.ui.DetailsFragment
import com.cabbagebeyond.ui.collection.abilities.AbilityAttribute
import org.koin.android.ext.android.inject

class AbilityDetailsFragment : DetailsFragment() {

    companion object {
        fun newInstance() = AbilityDetailsFragment()
    }

    private val _viewModel: AbilityDetailsViewModel
        get() = viewModel as AbilityDetailsViewModel

    private lateinit var _binding: FragmentAbilityDetailsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentAbilityDetailsBinding.inflate(inflater)

        val ability = AbilityDetailsFragmentArgs.fromBundle(requireArguments()).ability

        val dataSource: AbilityDataSource by inject()
        val worldDataSource: WorldDataSource by inject()
        viewModel = AbilityDetailsViewModel(
            ability,
            dataSource,
            worldDataSource,
            UserService.currentUser,
            requireActivity().application
        )

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

        _viewModel.attributes.observe(viewLifecycleOwner) { attributes ->
            setupAttributeSpinner(attributes.selected, attributes.values)
        }

        _viewModel.worlds.observe(viewLifecycleOwner) { worlds ->
            setupWorldSpinner(worlds.selected, worlds.values)
        }

        _viewModel.message.observe(viewLifecycleOwner) {
            it?.let {
                showSnackbar(resources.getString(it))
            }
        }

        setHasOptionsMenu(true)

        return _binding.root
    }

    private fun setupWorldSpinner(preSelection: World?, worlds: List<World?>) {
        if (worlds.isNullOrEmpty()) return

        setupSpinner(
            preSelection?.name,
            worlds.mapNotNull { it?.name ?: "" },
            _binding.worldSpinner
        ) { index ->
            val world = worlds.getOrNull(index)
            _viewModel.onWorldSelected(world)
        }
    }

    private fun setupAttributeSpinner(preSelection: AbilityAttribute?, attributes: List<AbilityAttribute>) {
        if (attributes.isNullOrEmpty()) return

        setupSpinner(
            preSelection?.title,
            attributes.map { it.title },
            _binding.attributeSpinner
        ) { index ->
            _viewModel.onAttributeSelected(attributes[index])
        }
    }

    override fun navigateToOcr() {
        findNavController().navigate(
            AbilityDetailsFragmentDirections.actionAbilityDetailsToOcr(
                _viewModel.properties
            )
        )
    }
}
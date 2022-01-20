package com.cabbagebeyond.ui.collection.abilities.details

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import com.cabbagebeyond.data.AbilityDataSource
import com.cabbagebeyond.databinding.AbilityDetailsFragmentBinding
import com.cabbagebeyond.model.User
import com.cabbagebeyond.util.Feature
import org.koin.android.ext.android.inject

class AbilityDetailsFragment : Fragment() {

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
        _viewModel = AbilityDetailsViewModel(ability, dataSource, User("", "", listOf(Feature.CONFIGURE_APP.name), listOf(), ""))

        _binding.viewModel = _viewModel
        _binding.ability = _viewModel.ability.value

        _viewModel.fabImage.observe(viewLifecycleOwner, Observer {
            it?.let {
                _binding.floatingActionButton.setImageResource(it)
            }
        })

        _viewModel.isEditing.observe(viewLifecycleOwner, Observer {
            it?.let {
                _binding.editName.isEnabled = it
                _binding.attributeSpinner.isEnabled = it
                _binding.worldSpinner.isEnabled = it
                _binding.description.isEnabled = it
            }
        })

        return _binding.root
    }

}
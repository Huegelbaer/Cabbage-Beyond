package com.cabbagebeyond.ui.collection.abilities

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.cabbagebeyond.data.AbilityDataSource
import com.cabbagebeyond.databinding.FragmentAbilitiesListBinding
import com.cabbagebeyond.model.Ability
import org.koin.android.ext.android.inject

class AbilitiesListFragment : Fragment() {

    private val _viewModel: AbilitiesViewModel by lazy {
        val dataSource: AbilityDataSource by inject()
        AbilitiesViewModel(dataSource)
    }

    private lateinit var _binding: FragmentAbilitiesListBinding
    private lateinit var _adapter: AbilitiesRecyclerViewAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAbilitiesListBinding.inflate(inflater)

        val clickListener = AbilityClickListener {
            _viewModel.onAbilityClicked(it)
        }
        _adapter = AbilitiesRecyclerViewAdapter(clickListener)

        _binding.list.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = _adapter
        }

        _viewModel.items.observe(viewLifecycleOwner, Observer {
            it?.let {
                _adapter.submitList(it)
            }
        })

        _viewModel.selectedAbility.observe(viewLifecycleOwner, Observer {
            it?.let {
                showDetails(it)
            }
        })

        return _binding.root
    }

    private fun showDetails(ability: Ability) {
        findNavController().navigate(AbilitiesListFragmentDirections.actionAbilitiesToDetails(ability))
        _viewModel.onNavigationCompleted()
    }
}
package com.cabbagebeyond.ui.collection.races

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.cabbagebeyond.data.RaceDataSource
import com.cabbagebeyond.databinding.FragmentRacesListBinding
import com.cabbagebeyond.model.Race
import org.koin.android.ext.android.inject

class RacesFragment : Fragment() {

    private val _viewModel: RacesViewModel by lazy {
        val dataSource: RaceDataSource by inject()
        RacesViewModel(dataSource)
    }

    private lateinit var _binding: FragmentRacesListBinding
    private lateinit var _adapter: RacesRecyclerViewAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRacesListBinding.inflate(inflater)

        val clickListener = RaceClickListener {
            _viewModel.onRaceClicked(it)
        }
        _adapter = RacesRecyclerViewAdapter(clickListener)

        _binding.list.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = _adapter
        }

        _viewModel.items.observe(viewLifecycleOwner, Observer {
            it?.let {
                _adapter.submitList(it)
            }
        })

        _viewModel.selectedRace.observe(viewLifecycleOwner, Observer {
            it?.let {
                showDetails(it)
            }
        })

        return _binding.root
    }

    private fun showDetails(race: Race) {
        findNavController().navigate(RacesFragmentDirections.actionRacesToDetails(race))
        _viewModel.onNavigationCompleted()
    }
}
package com.cabbagebeyond.ui.collection.handicaps

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.cabbagebeyond.data.HandicapDataSource
import com.cabbagebeyond.databinding.FragmentHandicapsListBinding
import com.cabbagebeyond.model.Handicap
import org.koin.android.ext.android.inject

class HandicapsFragment : Fragment() {

    private val _viewModel: HandicapsViewModel by lazy {
        val dataSource: HandicapDataSource by inject()
        HandicapsViewModel(dataSource)
    }

    private lateinit var _binding: FragmentHandicapsListBinding
    private lateinit var _adapter: HandicapsRecyclerViewAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHandicapsListBinding.inflate(inflater)

        val clickListener = HandicapClickListener {
            _viewModel.onHandicapClicked(it)
        }
        _adapter = HandicapsRecyclerViewAdapter(clickListener)

        _binding.list.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = _adapter
        }

        _viewModel.items.observe(viewLifecycleOwner) {
            it?.let {
                _adapter.submitList(it)
            }
        }

        _viewModel.selectedHandicap.observe(viewLifecycleOwner) {
            it?.let {
                showDetails(it)
            }
        }

        return _binding.root
    }

    private fun showDetails(handicap: Handicap) {
        findNavController().navigate(HandicapsFragmentDirections.actionHandicapsToDetails(handicap))
        _viewModel.onNavigationCompleted()
    }
}
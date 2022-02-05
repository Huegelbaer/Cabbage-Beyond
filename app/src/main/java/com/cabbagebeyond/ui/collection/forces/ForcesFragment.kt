package com.cabbagebeyond.ui.collection.forces

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.cabbagebeyond.data.ForceDataSource
import com.cabbagebeyond.databinding.FragmentForcesListBinding
import com.cabbagebeyond.model.Force
import com.cabbagebeyond.ui.collection.forces.details.ForceDetailsFragmentArgs
import org.koin.android.ext.android.inject

class ForcesFragment : Fragment() {

    private val _viewModel: ForcesViewModel by lazy {
        val dataSource: ForceDataSource by inject()
        ForcesViewModel(dataSource)
    }

    private lateinit var _binding: FragmentForcesListBinding
    private lateinit var _adapter: ForcesRecyclerViewAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentForcesListBinding.inflate(inflater)

        val clickListener = ForceClickListener {
            _viewModel.onForceClicked(it)
        }
        _adapter = ForcesRecyclerViewAdapter(clickListener)

        _binding.list.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = _adapter
        }

        _viewModel.items.observe(viewLifecycleOwner, Observer {
            it?.let {
                _adapter.submitList(it)
            }
        })

        _viewModel.selectedForce.observe(viewLifecycleOwner, Observer {
            it?.let {
                showDetails(it)
            }
        })

        return _binding.root
    }

    private fun showDetails(force: Force) {
        findNavController().navigate(ForcesFragmentDirections.actionForcesToDetails(force))
        _viewModel.onNavigationCompleted()
    }
}
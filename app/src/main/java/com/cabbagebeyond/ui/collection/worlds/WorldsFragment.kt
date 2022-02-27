package com.cabbagebeyond.ui.collection.worlds

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.cabbagebeyond.data.WorldDataSource
import com.cabbagebeyond.databinding.FragmentWorldsListBinding
import com.cabbagebeyond.model.World
import org.koin.android.ext.android.inject


class WorldsFragment : Fragment() {

    private val _viewModel: WorldsViewModel by lazy {
        val dataSource: WorldDataSource by inject()
        WorldsViewModel(dataSource)
    }

    private lateinit var _binding: FragmentWorldsListBinding
    private lateinit var _adapter: WorldRecyclerViewAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentWorldsListBinding.inflate(inflater)

        val clickListener = WorldClickListener {
            _viewModel.onSelectWorld(it)
        }
        _adapter = WorldRecyclerViewAdapter(clickListener)

        _binding.list.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = _adapter
        }

        _viewModel.items.observe(viewLifecycleOwner) { worlds ->
            worlds?.let {
                _adapter.submitList(it)
            }
        }

        _viewModel.selectedWorld.observe(viewLifecycleOwner) { world ->
            world?.let {
                navigateToDetails(it)
            }
        }

        return _binding.root
    }

    private fun navigateToDetails(world: World) {
        findNavController().navigate(WorldsFragmentDirections.actionHomeToWorldDetails(world))
        _viewModel.onNavigationCompleted()
    }
}
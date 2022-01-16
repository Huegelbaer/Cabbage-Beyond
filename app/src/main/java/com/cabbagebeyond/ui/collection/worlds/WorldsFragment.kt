package com.cabbagebeyond.ui.collection.worlds

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.cabbagebeyond.R
import com.cabbagebeyond.model.World

/**
 * A fragment representing a list of Items.
 */
class WorldsFragment : Fragment() {

    private val _viewModel: WorldsViewModel by activityViewModels()

    private var columnCount = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            columnCount = it.getInt(ARG_COLUMN_COUNT)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_worlds_list, container, false)

        val clickListener = WorldClickListener {
            _viewModel.onSelectWorld(it)
        }

        val worldsAdapter = WorldRecyclerViewAdapter(clickListener)

        // Set the adapter
        if (view is RecyclerView) {
            with(view) {
                layoutManager = LinearLayoutManager(context)
                adapter = worldsAdapter
            }
        }

        _viewModel.items.observe(viewLifecycleOwner, Observer { worlds ->
            worlds?.let {
                worldsAdapter.submitList(it)
            }
        })

        _viewModel.selectedWorld.observe(viewLifecycleOwner, Observer { world ->
            world?.let {
                navigateToDetails(it)
            }
        })

        return view
    }

    private fun navigateToDetails(world: World) {
        findNavController().navigate(WorldsFragmentDirections.actionHomeToWorldDetails(world))
        _viewModel.onNavigationCompleted()
    }

    companion object {

        // TODO: Customize parameter argument names
        const val ARG_COLUMN_COUNT = "column-count"

        // TODO: Customize parameter initialization
        @JvmStatic
        fun newInstance(columnCount: Int) =
            WorldsFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_COLUMN_COUNT, columnCount)
                }
            }
    }
}
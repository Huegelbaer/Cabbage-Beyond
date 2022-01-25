package com.cabbagebeyond.ui.collection.talents

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.cabbagebeyond.data.TalentDataSource
import com.cabbagebeyond.databinding.FragmentTalentsListBinding
import com.cabbagebeyond.model.Talent
import org.koin.android.ext.android.inject

class TalentsListFragment : Fragment() {

    private val _viewModel: TalentsViewModel by lazy {
        val dataSource: TalentDataSource by inject()
        TalentsViewModel(dataSource)
    }

    private lateinit var _binding: FragmentTalentsListBinding
    private lateinit var _adapter: TalentsRecyclerViewAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTalentsListBinding.inflate(inflater)

        val clickListener = TalentClickListener {
            _viewModel.onTalentClicked(it)
        }
        _adapter = TalentsRecyclerViewAdapter(clickListener)

        _binding.list.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = _adapter
        }

        _viewModel.items.observe(viewLifecycleOwner, Observer {
            it?.let {
                _adapter.submitList(it)
            }
        })

        _viewModel.selectedTalent.observe(viewLifecycleOwner, Observer {
            it?.let {
                showDetails(it)
            }
        })

        return _binding.root
    }

    private fun showDetails(talent: Talent) {
        findNavController().navigate(TalentsListFragmentDirections.actionTalentsToDetails(talent))
        _viewModel.onNavigationCompleted()
    }
}
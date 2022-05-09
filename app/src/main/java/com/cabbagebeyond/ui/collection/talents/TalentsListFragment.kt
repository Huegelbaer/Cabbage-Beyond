package com.cabbagebeyond.ui.collection.talents

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.cabbagebeyond.FilterDialogFragment
import com.cabbagebeyond.data.TalentDataSource
import com.cabbagebeyond.databinding.FragmentTalentsListBinding
import com.cabbagebeyond.model.Talent
import com.cabbagebeyond.model.World
import com.cabbagebeyond.ui.collection.CollectionListFragment
import com.cabbagebeyond.ui.collection.CollectionListViewModel
import org.koin.android.ext.android.inject


class TalentsListFragment : CollectionListFragment() {

    private val _viewModel: TalentsViewModel
        get() = viewModel as TalentsViewModel

    private lateinit var _binding: FragmentTalentsListBinding
    private lateinit var _adapter: TalentsRecyclerViewAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTalentsListBinding.inflate(inflater)

        val dataSource: TalentDataSource by inject()
        viewModel = TalentsViewModel(requireActivity().application, dataSource)

        val clickListener = TalentClickListener {
            _viewModel.onTalentClicked(it)
        }
        _adapter = TalentsRecyclerViewAdapter(clickListener)

        _binding.list.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = _adapter
        }

        _viewModel.items.observe(viewLifecycleOwner) {
            it?.let {
                _adapter.submitList(it)
            }
        }

        _viewModel.selectedTalent.observe(viewLifecycleOwner) {
            it?.let {
                showDetails(it)
            }
        }

        _viewModel.interaction.observe(viewLifecycleOwner) {
            it?.let {
                when (it) {
                    is TalentsViewModel.Interaction.OpenFilter -> {
                        showFilterDialog(it.types, it.ranks, it.worlds)
                    }
                }
                _viewModel.onInteractionCompleted()
            }
        }

        setHasOptionsMenu(true)

        return _binding.root
    }

    private fun showFilterDialog(types: CollectionListViewModel.FilterData<TalentType>, ranks: CollectionListViewModel.FilterData<TalentRank>, worlds: CollectionListViewModel.FilterData<World>) {

        var selectedType = types.selected
        var selectedRank = ranks.selected
        var selectedWorld = worlds.selected

        val dialog = FilterDialogFragment(onFilter = {
            _viewModel.filter(selectedType, selectedRank, selectedWorld)
        })

        dialog.addFilterChipGroup(types.title, types.values, types.selected, types.titleProperty) {
            selectedType = it
        }
        dialog.addFilterChipGroup(ranks.title, ranks.values, ranks.selected, ranks.titleProperty) {
            selectedRank = it
        }
        dialog.addFilterChipGroup(worlds.title, worlds.values, worlds.selected, worlds.titleProperty) {
            selectedWorld = it
        }

        dialog.show(requireActivity().supportFragmentManager, "talent_dialog_filter")
    }

    private fun showDetails(talent: Talent) {
        findNavController().navigate(TalentsListFragmentDirections.actionTalentsToDetails(talent))
        _viewModel.onNavigationCompleted()
    }
}
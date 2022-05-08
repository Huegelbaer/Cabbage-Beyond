package com.cabbagebeyond.ui.collection.talents

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.navigation.fragment.findNavController
import com.cabbagebeyond.R
import com.cabbagebeyond.data.TalentDataSource
import com.cabbagebeyond.databinding.FragmentTalentsListBinding
import com.cabbagebeyond.model.Talent
import com.cabbagebeyond.model.World
import com.cabbagebeyond.ui.collection.CollectionListFragment
import com.cabbagebeyond.ui.collection.CollectionListViewModel
import com.google.android.material.chip.ChipGroup
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
        val viewInflated = layoutInflater.inflate(R.layout.content_view_talents_filter, null)
        val typesChipGroup = viewInflated.findViewById<ChipGroup>(R.id.filter_type_chip_group)
        val ranksChipGroup = viewInflated.findViewById<ChipGroup>(R.id.filter_rank_chip_group)
        val worldsChipGroup = viewInflated.findViewById<ChipGroup>(R.id.filter_world_chip_group)

        prepareChipGroup(typesChipGroup, types)
        prepareChipGroup(ranksChipGroup, ranks)
        prepareChipGroup(worldsChipGroup, worlds)

        AlertDialog.Builder(requireContext())
            .setTitle(R.string.menu_filter)
            .setView(viewInflated)
            .setPositiveButton(R.string.menu_filter) { _, _ ->
                val selectedType = types.values.getOrNull(typesChipGroup.checkedChipId)
                val selectedRank = ranks.values.getOrNull(typesChipGroup.checkedChipId)
                val selectedWorld = worlds.values.getOrNull(worldsChipGroup.checkedChipId)
                _viewModel.filter(selectedType, selectedRank, selectedWorld)
            }
            .setNegativeButton(R.string.dialog_button_cancel, null)
            .show()
    }

    private fun showDetails(talent: Talent) {
        findNavController().navigate(TalentsListFragmentDirections.actionTalentsToDetails(talent))
        _viewModel.onNavigationCompleted()
    }
}
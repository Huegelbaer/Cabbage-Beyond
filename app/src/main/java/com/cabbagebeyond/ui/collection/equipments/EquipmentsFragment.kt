package com.cabbagebeyond.ui.collection.equipments

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.cabbagebeyond.data.AbilityDataSource
import com.cabbagebeyond.data.EquipmentDataSource
import com.cabbagebeyond.databinding.FragmentAbilitiesListBinding
import com.cabbagebeyond.databinding.FragmentEquipmentsListBinding
import com.cabbagebeyond.model.Equipment
import com.cabbagebeyond.ui.collection.abilities.AbilitiesRecyclerViewAdapter
import com.cabbagebeyond.ui.collection.abilities.AbilitiesViewModel
import com.cabbagebeyond.ui.collection.abilities.AbilityClickListener
import org.koin.android.ext.android.inject


class EquipmentsFragment : Fragment() {

    private val _viewModel: EquipmentsViewModel by lazy {
        val dataSource: EquipmentDataSource by inject()
        EquipmentsViewModel(dataSource)
    }

    private lateinit var _binding: FragmentEquipmentsListBinding
    private lateinit var _adapter: EquipmentsRecyclerViewAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEquipmentsListBinding.inflate(inflater)

        val clickListener = EquipmentClickListener {
            _viewModel.onEquipmentClicked(it)
        }
        _adapter = EquipmentsRecyclerViewAdapter(clickListener)

        _binding.list.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = _adapter
        }

        _viewModel.items.observe(viewLifecycleOwner) {
            it?.let {
                _adapter.submitList(it)
            }
        }

        _viewModel.selectedEquipment.observe(viewLifecycleOwner) {
            it?.let {
                showDetails(it)
            }
        }

        return _binding.root
    }

    private fun showDetails(equipment: Equipment) {
        findNavController().navigate(EquipmentsFragmentDirections.actionEquipmentsToDetails(equipment))
        _viewModel.onNavigationCompleted()
    }
}
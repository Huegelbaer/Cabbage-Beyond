package com.cabbagebeyond.ui.collection.forces.details

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.lifecycle.Observer
import com.cabbagebeyond.data.ForceDataSource
import com.cabbagebeyond.data.WorldDataSource
import com.cabbagebeyond.databinding.ForceDetailsFragmentBinding
import com.cabbagebeyond.model.User
import com.cabbagebeyond.model.World
import com.cabbagebeyond.util.Feature
import com.google.android.material.snackbar.Snackbar
import org.koin.android.ext.android.inject

class ForceDetailsFragment : Fragment() {

    companion object {
        fun newInstance() = ForceDetailsFragment()
    }

    private lateinit var _viewModel: ForceDetailsViewModel

    private lateinit var _binding: ForceDetailsFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = ForceDetailsFragmentBinding.inflate(inflater)

        val force = ForceDetailsFragmentArgs.fromBundle(requireArguments()).force

        val dataSource: ForceDataSource by inject()
        val worldDataSource: WorldDataSource by inject()
        _viewModel = ForceDetailsViewModel(
            force, dataSource, worldDataSource, User(
                "", "", listOf(
                    Feature.CONFIGURE_APP.name
                ), listOf(), ""
            ), requireActivity().application
        )

        _binding.viewModel = _viewModel
        _binding.lifecycleOwner = this

        _viewModel.fabImage.observe(viewLifecycleOwner, Observer {
            it?.let {
                _binding.floatingActionButton.setImageResource(it)
            }
        })

        _viewModel.isEditing.observe(viewLifecycleOwner, Observer {
            it?.let { isEditing ->
                if (isEditing) {
                    _binding.readGroup.visibility = View.INVISIBLE
                    _binding.editGroup.visibility = View.VISIBLE
                } else {
                    _binding.readGroup.visibility = View.VISIBLE
                    _binding.editGroup.visibility = View.INVISIBLE
                }
            }
        })

        _viewModel.ranks.observe(viewLifecycleOwner, Observer {
            val list = it ?: listOf()
            setupRankSpinner(force.rangRequirement, list)
        })

        _viewModel.worlds.observe(viewLifecycleOwner, Observer {
            val list = it ?: listOf()
            setupWorldSpinner(force.world, list)
        })

        _viewModel.message.observe(viewLifecycleOwner, Observer {
            it?.let {
                showSnackbar(resources.getString(it))
            }
        })

        return _binding.root
    }

    private fun setupRankSpinner(attribute: String, attributes: List<String>) {
        setupSpinner(attribute, attributes, _binding.requirementSpinner, object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parentView: AdapterView<*>?,
                selectedItemView: View?,
                position: Int,
                id: Long
            ) {
                val item = attributes[position]
                _viewModel.onRankSelected(item)
            }

            override fun onNothingSelected(parentView: AdapterView<*>?) {
                // your code here
            }
        })
    }

    private fun setupWorldSpinner(world: World?, worlds: List<World?>) {
        val titles = worlds.map { it?.name ?: "" }

        setupSpinner(world?.name, titles, _binding.worldSpinner, object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parentView: AdapterView<*>?,
                selectedItemView: View?,
                position: Int,
                id: Long
            ) {
                val item = worlds[position]
                _viewModel.onWorldSelected(item)
            }

            override fun onNothingSelected(parentView: AdapterView<*>?) {
                // your code here
            }
        })
    }

    private fun setupSpinner(
        selectedItem: Any?,
        items: List<Any?>,
        spinner: Spinner,
        listener: AdapterView.OnItemSelectedListener
    ) {
        ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item,
            items
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner.adapter = adapter
        }
        val position = items.indexOf(selectedItem)
        spinner.setSelection(position)

        spinner.onItemSelectedListener = listener
    }

    private fun showSnackbar(message: String) {
        Snackbar
            .make(requireView(), message, Snackbar.LENGTH_LONG)
            .show()
    }
}


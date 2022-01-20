package com.cabbagebeyond.ui.collection.worlds.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.cabbagebeyond.R
import com.cabbagebeyond.databinding.FragmentWorldDetailsBinding

class WorldDetailsFragment : Fragment() {

    private lateinit var _binding: FragmentWorldDetailsBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_world_details,
            container,
            false
        )

        val world = WorldDetailsFragmentArgs.fromBundle(requireArguments()).world
        _binding.world = world

        return _binding.root
    }
}
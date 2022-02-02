package com.cabbagebeyond.ui.ocr

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.cabbagebeyond.databinding.FragmentTextRecognizerBinding


class TextRecognizerFragment : Fragment() {

    private lateinit var _binding: FragmentTextRecognizerBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTextRecognizerBinding.inflate(inflater)

        _binding.cameraButton.setOnClickListener {
        }

        _binding.galleryButton.setOnClickListener {
        }

        return _binding.root
    }

}
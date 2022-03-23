package com.cabbagebeyond.ui.onboarding

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.cabbagebeyond.databinding.FragmentOnboardingBinding

class OnboardingFragment : Fragment() {

    companion object {
        fun newInstance() = OnboardingFragment()
    }

    private lateinit var _viewModel: OnboardingViewModel
    private lateinit var _binding: FragmentOnboardingBinding
    private lateinit var _adapter: OnboardingPageAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _viewModel = ViewModelProvider(this).get(OnboardingViewModel::class.java)
        _adapter = OnboardingPageAdapter(_viewModel.items)
        _binding = FragmentOnboardingBinding.inflate(inflater)
        _binding.viewpager.adapter = _adapter

        return _binding.root
    }

}
package com.cabbagebeyond.ui.onboarding

import android.app.AlertDialog
import android.app.Dialog
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager2.widget.ViewPager2
import com.cabbagebeyond.R
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

        _binding.viewpager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
                val progress = (position + positionOffset) / (_viewModel.items.size - 1)
                _binding.root.progress = progress
                updateTextOfNextButton(progress)
            }
        })

        _binding.buttonSkip.setOnClickListener {
            finishOnboarding()
        }

        _binding.buttonNext.setOnClickListener {
            if (_binding.root.progress == 1f) {
                finishOnboarding()
            } else {
                scrollToNextPage()
            }
        }

        _binding.buttonPrevious.setOnClickListener {
            scrollToPreviousPage()
        }

        return _binding.root
    }

    private fun updateTextOfNextButton(progress: Float) {
        _binding.buttonNext.text = if (progress == 1f) {
            resources.getString(R.string.button_finish)
        } else {
            resources.getString(R.string.button_next)
        }
    }

    private fun scrollToNextPage() {
        _binding.viewpager.currentItem += 1
    }

    private fun scrollToPreviousPage() {
        _binding.viewpager.currentItem -= 1
    }

    private fun finishOnboarding() {
        AlertDialog.Builder(context).setPositiveButton("OK") { _, _ -> }
            .show()
    }
}
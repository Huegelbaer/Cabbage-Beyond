package com.cabbagebeyond.ui.onboarding

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.cabbagebeyond.databinding.ContentViewOnboardingPageBinding

class OnboardingPageAdapter(private val items: List<OnboardingViewModel.PageModel>): RecyclerView.Adapter<OnboardingPageAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int = items.size

    class ViewHolder(private val binding: ContentViewOnboardingPageBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: OnboardingViewModel.PageModel) {
            binding.model = item
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding =
                    ContentViewOnboardingPageBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }
    }
}
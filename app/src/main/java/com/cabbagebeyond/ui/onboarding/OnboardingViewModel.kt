package com.cabbagebeyond.ui.onboarding

import androidx.lifecycle.ViewModel
import com.cabbagebeyond.R

class OnboardingViewModel : ViewModel() {

    inner class PageModel(val title: Int, val description: Int, val icon: Int)

    val items: List<PageModel> = listOf(
        PageModel(R.string.onboarding_collection_title, R.string.onboarding_collection_description, R.drawable.ic_collections_bookmark),
        PageModel(R.string.onboarding_roles_title, R.string.onboarding_roles_description, R.drawable.ic_groups),
        PageModel(R.string.onboarding_ocr_title, R.string.onboarding_ocr_description, R.drawable.ic_document_scanner_big),
        PageModel(R.string.onboarding_stories_coming_soon_title, R.string.onboarding_stories_coming_soon_description, R.drawable.ic_history_edu)
    )


}
package com.cabbagebeyond.ui.collection

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import kotlin.reflect.KProperty1

open class CollectionListViewModel(app: Application) : AndroidViewModel(app) {

    class FilterData<T: Any>(var title: String, var values: List<T>, var selected: T?, var titleProperty: KProperty1<T, String>)

    open fun onSearch(query: String) {
    }

    open fun onSearchCanceled() {
    }

    open fun onSelectFilter() {
    }
}
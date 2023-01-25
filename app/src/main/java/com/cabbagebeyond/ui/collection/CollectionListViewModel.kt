package com.cabbagebeyond.ui.collection

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.cabbagebeyond.R
import com.cabbagebeyond.model.User
import com.cabbagebeyond.util.Feature
import kotlin.reflect.KProperty1

abstract class CollectionListViewModel<T>(user: User, app: Application):
    AndroidViewModel(app) {

    class FilterData<T : Any>(
        var title: String,
        var values: List<T>,
        var selected: T?,
        var titleProperty: KProperty1<T, String>
    )

    data class EmptyListState(
        val title: String,
        val message: String,
        val button: String?,
        val action: (() -> Unit)?
    )

    private var _emptyListState = MutableLiveData<EmptyListState?>()
    val emptyListState: LiveData<EmptyListState?>
        get() = _emptyListState

    private val _userCanAddNewContent = MutableLiveData<Boolean>()
    val userCanAddNewContent: LiveData<Boolean>
        get() = _userCanAddNewContent

    private var _selectedItem = MutableLiveData<Pair<T, Boolean>?>()
    val selectedItem: LiveData<Pair<T, Boolean>?>
        get() = _selectedItem

    protected var mutableItems = MutableLiveData<List<T>>()
    val items: LiveData<List<T>>
        get() = mutableItems

    init {
        _userCanAddNewContent.value = user.features.contains(Feature.CONFIGURE_APP.name)
    }

    // region Empty state

    protected fun showNoContentAvailable() {
        val resources = getApplication<Application>().resources
        _emptyListState.value = EmptyListState(
            resources.getString(R.string.empty_state_list_title),
            resources.getString(R.string.empty_state_list_message),
            resources.getString(R.string.empty_state_list_reset_button),
            null
        )
    }

    protected fun showNoFilterResult(searchTerms: List<String>, resetAction: () -> Unit) {
        val result = searchTerms.joinToString(" & ") { "'$it'" }
        val resources = getApplication<Application>().resources
        _emptyListState.value = EmptyListState(
            resources.getString(R.string.empty_state_filtered_list_title),
            resources.getString(R.string.empty_state_filtered_list_message, result),
            resources.getString(R.string.empty_state_filtered_list_reset_button)
        ) {
            resetAction()
            resetEmptyState()
        }
    }

    protected fun resetEmptyState() {
        _emptyListState.value = null
    }

    // endregion

    // region Filter interactions

    open fun onSearch(query: String) {
    }

    open fun onSearchCanceled() {
    }

    open fun onSelectFilter() {
    }

    // endregion

    // region Navigation

    fun onItemSelected(item: T) {
        _selectedItem.value = Pair(item, false)
    }

    fun onCreateNewItem(item: T) {
        _selectedItem.value = Pair(item, true)
    }

    fun onNavigationCompleted() {
        _selectedItem.value = null
    }

    // endregion
}
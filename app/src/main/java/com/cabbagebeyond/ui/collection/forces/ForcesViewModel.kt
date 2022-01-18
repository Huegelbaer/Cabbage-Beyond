package com.cabbagebeyond.ui.collection.forces

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cabbagebeyond.data.ForceDataSource
import com.cabbagebeyond.model.Force
import kotlinx.coroutines.launch

class ForcesViewModel(
    private val forceDataSource: ForceDataSource
) : ViewModel() {

    private var _items = MutableLiveData<List<Force>>()
    val items: LiveData<List<Force>>
        get() = _items

    private var _selectedForce = MutableLiveData<Force?>()
    val selectedForce: LiveData<Force?>
        get() = _selectedForce

    init {
        viewModelScope.launch {
            _items.value = forceDataSource.getForces().getOrDefault(listOf())
        }
    }

    fun onForceClicked(force: Force) {
        _selectedForce.value = force
    }

    fun onNavigationCompleted() {
        _selectedForce.value = null
    }
}
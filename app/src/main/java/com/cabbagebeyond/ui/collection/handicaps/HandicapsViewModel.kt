package com.cabbagebeyond.ui.collection.handicaps

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cabbagebeyond.data.HandicapDataSource
import com.cabbagebeyond.model.Handicap
import kotlinx.coroutines.launch

class HandicapsViewModel(
    private val handicapsDataSource: HandicapDataSource
) : ViewModel() {

    private var _items = MutableLiveData<List<Handicap>>()
    val items: LiveData<List<Handicap>>
        get() = _items

    private var _selectedHandicap = MutableLiveData<Handicap?>()
    val selectedHandicap: LiveData<Handicap?>
        get() = _selectedHandicap

    init {
        viewModelScope.launch {
            _items.value = handicapsDataSource.getHandicaps().getOrDefault(listOf())
        }
    }

    fun onHandicapClicked(handicap: Handicap) {
        _selectedHandicap.value = handicap
    }

    fun onNavigationCompleted() {
        _selectedHandicap.value = null
    }
}
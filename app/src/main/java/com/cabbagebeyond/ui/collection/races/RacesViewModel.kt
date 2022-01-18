package com.cabbagebeyond.ui.collection.races

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cabbagebeyond.data.RaceDataSource
import com.cabbagebeyond.model.Race
import kotlinx.coroutines.launch

class RacesViewModel(
    private val raceDataSource: RaceDataSource
) : ViewModel() {

    private var _items = MutableLiveData<List<Race>>()
    val items: LiveData<List<Race>>
        get() = _items

    private var _selectedRace = MutableLiveData<Race?>()
    val selectedRace: LiveData<Race?>
        get() = _selectedRace

    init {
        viewModelScope.launch {
            _items.value = raceDataSource.getRaces().getOrDefault(listOf())
        }
    }

    fun onRaceClicked(race: Race) {
        _selectedRace.value = race
    }

    fun onNavigationCompleted() {
        _selectedRace.value = null
    }
}
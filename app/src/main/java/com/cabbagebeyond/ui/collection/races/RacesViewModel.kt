package com.cabbagebeyond.ui.collection.races

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.cabbagebeyond.R
import com.cabbagebeyond.data.RaceDataSource
import com.cabbagebeyond.model.Race
import com.cabbagebeyond.model.World
import com.cabbagebeyond.ui.collection.CollectionListViewModel
import kotlinx.coroutines.launch

class RacesViewModel(
    application: Application,
    private val raceDataSource: RaceDataSource
) : CollectionListViewModel(application) {

    object Filter {
        var selectedWorld: World? = null
    }

    sealed class Interaction {
        data class OpenFilter(val worlds: FilterData<World>) : Interaction()
    }

    private var _races = listOf<Race>()
    private var _items = MutableLiveData<List<Race>>()
    val items: LiveData<List<Race>>
        get() = _items

    private var _selectedRace = MutableLiveData<Race?>()
    val selectedRace: LiveData<Race?>
        get() = _selectedRace

    private var _interaction = MutableLiveData<Interaction?>()
    val interaction: LiveData<Interaction?>
        get() = _interaction

    private var _activeFilter = Filter

    init {
        viewModelScope.launch {
            _races = raceDataSource.getRaces().getOrDefault(listOf())
            _items.value = _races
            if (_races.isEmpty()) {
                showNoContentAvailable()
            }
        }
    }

    fun onRaceClicked(race: Race) {
        _selectedRace.value = race
    }

    fun onNavigationCompleted() {
        _selectedRace.value = null
    }

    override fun onSelectFilter() {
        val application = getApplication<Application>()
        val worlds = _races.mapNotNull { it.world }.toSet().toList()

        _interaction.value = Interaction.OpenFilter(
            FilterData(application.resources.getString(R.string.character_world), worlds, _activeFilter.selectedWorld, World::name)
        )
    }

    fun filter(world: World?) {
        _activeFilter.selectedWorld = world

        viewModelScope.launch {
            val filteredItems = _races.filter { handicap ->
                world?.let { world ->
                    handicap.world == world
                } ?: true
            }
            _items.value = filteredItems
            if (filteredItems.isEmpty()) {
                val searchTerm = listOfNotNull(world?.name)
                showNoFilterResult(searchTerm) {
                    resetFilter()
                }
            } else {
                resetEmptyState()
            }
        }
    }

    private fun resetFilter() {
        _activeFilter.selectedWorld = null
        _items.value = _races
    }

    fun onInteractionCompleted() {
        _interaction.value = null
    }
}
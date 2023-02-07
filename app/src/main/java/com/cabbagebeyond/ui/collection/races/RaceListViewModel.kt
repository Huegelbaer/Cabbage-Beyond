package com.cabbagebeyond.ui.collection.races

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.cabbagebeyond.R
import com.cabbagebeyond.data.RaceDataSource
import com.cabbagebeyond.model.Race
import com.cabbagebeyond.model.User
import com.cabbagebeyond.model.World
import com.cabbagebeyond.ui.collection.CollectionListViewModel
import kotlinx.coroutines.launch
import java.util.*

class RaceListViewModel(
    user: User,
    application: Application,
    private val raceDataSource: RaceDataSource
) : CollectionListViewModel<Race>(user, application) {

    object Filter {
        var selectedWorld: World? = null
    }

    sealed class Interaction {
        data class OpenFilter(val worlds: FilterData<World>) : Interaction()
    }

    private var _races = listOf<Race>()

    private var _interaction = MutableLiveData<Interaction?>()
    val interaction: LiveData<Interaction?>
        get() = _interaction

    private var _activeFilter = Filter

    init {
        viewModelScope.launch {
            _races = raceDataSource.getRaces().getOrDefault(listOf())
            mutableItems.value = _races
            if (_races.isEmpty()) {
                showNoContentAvailable()
            }
        }
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
            mutableItems.value = filteredItems
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
        mutableItems.value = _races
    }

    fun onInteractionCompleted() {
        _interaction.value = null
    }


    fun addRace() {
        val newEquipment = Race("", "", listOf(), null, UUID.randomUUID().toString())
        onCreateNewItem(newEquipment)
    }

}
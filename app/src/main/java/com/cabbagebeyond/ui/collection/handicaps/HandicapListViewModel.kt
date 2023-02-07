package com.cabbagebeyond.ui.collection.handicaps

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.cabbagebeyond.R
import com.cabbagebeyond.data.HandicapDataSource
import com.cabbagebeyond.model.Handicap
import com.cabbagebeyond.model.User
import com.cabbagebeyond.model.World
import com.cabbagebeyond.ui.collection.CollectionListViewModel
import kotlinx.coroutines.launch
import java.util.*

class HandicapListViewModel(
    user: User,
    app: Application,
    private val handicapsDataSource: HandicapDataSource
) : CollectionListViewModel<Handicap>(user, app) {

    object Filter {
        var selectedType: HandicapType? = null
        var selectedWorld: World? = null
    }

    sealed class Interaction {
        data class OpenFilter(val types: FilterData<HandicapType>, val worlds: FilterData<World>) : Interaction()
    }

    private var _handicaps = listOf<Handicap>()

    private var _interaction = MutableLiveData<Interaction?>()
    val interaction: LiveData<Interaction?>
        get() = _interaction

    private var _activeFilter = Filter

    init {
        viewModelScope.launch {
            _handicaps = handicapsDataSource.getHandicaps().getOrDefault(listOf())
            mutableItems.value = _handicaps
            if (_handicaps.isEmpty()) {
                showNoContentAvailable()
            }
        }
    }

    override fun onSelectFilter() {
        val application = getApplication<Application>()
        val types = _handicaps.map { handicap -> handicap.type.let { HandicapType.create(it, application) } }.toSet().toList()
        val worlds = _handicaps.mapNotNull { it.world }.toSet().toList()

        _interaction.value = Interaction.OpenFilter(
            FilterData(application.resources.getString(R.string.character_type), types, _activeFilter.selectedType, HandicapType::title),
            FilterData(application.resources.getString(R.string.character_world), worlds, _activeFilter.selectedWorld, World::name)
        )
    }

    fun filter(type: HandicapType?, world: World?) {
        _activeFilter.selectedType = type
        _activeFilter.selectedWorld = world

        viewModelScope.launch {
            val filteredItems = _handicaps.filter { handicap ->
                val iType = type?.let { type ->
                    handicap.type == type.type
                } ?: true
                val iWorld = world?.let { world ->
                    handicap.world == world
                } ?: true

                iType && iWorld
            }
            mutableItems.value = filteredItems
            if (filteredItems.isEmpty()) {
                val searchTerm = listOfNotNull(type?.title, world?.name)
                showNoFilterResult(searchTerm) {
                    resetFilter()
                }
            } else {
                resetEmptyState()
            }
        }
    }

    private fun resetFilter() {
        _activeFilter.selectedType = null
        _activeFilter.selectedWorld = null
        mutableItems.value = _handicaps
    }

    fun onInteractionCompleted() {
        _interaction.value = null
    }

    fun addHandicap() {
        val newEquipment = Handicap("", "", Handicap.Type.SLIGHT_OR_HEAVY, null, UUID.randomUUID().toString())
        onCreateNewItem(newEquipment)
    }

}
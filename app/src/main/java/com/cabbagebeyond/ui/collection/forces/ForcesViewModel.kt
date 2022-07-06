package com.cabbagebeyond.ui.collection.forces

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.cabbagebeyond.R
import com.cabbagebeyond.data.ForceDataSource
import com.cabbagebeyond.model.Force
import com.cabbagebeyond.model.User
import com.cabbagebeyond.model.World
import com.cabbagebeyond.ui.collection.CollectionListViewModel
import kotlinx.coroutines.launch

class ForcesViewModel(
    user: User,
    application: Application,
    private val forceDataSource: ForceDataSource
) : CollectionListViewModel(user, application) {

    object Filter {
        var selectedRank: ForceRank? = null
        var selectedWorld: World? = null
    }

    sealed class Interaction {
        data class OpenFilter(val ranks: FilterData<ForceRank>, val worlds: FilterData<World>) : Interaction()
    }

    private var _forces = listOf<Force>()
    private var _items = MutableLiveData<List<Force>>()
    val items: LiveData<List<Force>>
        get() = _items

    private var _selectedForce = MutableLiveData<Force?>()
    val selectedForce: LiveData<Force?>
        get() = _selectedForce

    private var _interaction = MutableLiveData<Interaction?>()
    val interaction: LiveData<Interaction?>
        get() = _interaction

    private var _activeFilter = Filter

    init {
        viewModelScope.launch {
            _forces = forceDataSource.getForces().getOrDefault(listOf())
            _items.value = _forces
            if (_forces.isEmpty()) {
                showNoContentAvailable()
            }
        }
    }

    fun onForceClicked(force: Force) {
        _selectedForce.value = force
    }

    fun onNavigationCompleted() {
        _selectedForce.value = null
    }

    override fun onSelectFilter() {
        val application = getApplication<Application>()
        val ranks = _forces.mapNotNull { force -> force.rangRequirement?.let { ForceRank.create(it, application) } }.toSet().toList()
        val worlds = _forces.mapNotNull { it.world }.toSet().toList()

        _interaction.value = Interaction.OpenFilter(
            FilterData(application.resources.getString(R.string.talent_rang_requirement), ranks, _activeFilter.selectedRank, ForceRank::title),
            FilterData(application.resources.getString(R.string.character_world), worlds, _activeFilter.selectedWorld, World::name)
        )
    }

    fun filter(rank: ForceRank?, world: World?) {
        _activeFilter.selectedRank = rank
        _activeFilter.selectedWorld = world

        viewModelScope.launch {
            val filteredItems = _forces.filter { force ->
                val iRank = rank?.let { rank ->
                    force.rangRequirement == rank.rank
                } ?: true
                val iWorld = world?.let { world ->
                    force.world == world
                } ?: true

                iRank && iWorld
            }
            _items.value = filteredItems
            if (filteredItems.isEmpty()) {
                val searchTerm = listOfNotNull(rank?.title, world?.name)
                showNoFilterResult(searchTerm) {
                    resetFilter()
                }
            } else {
                resetEmptyState()
            }
        }
    }

    private fun resetFilter() {
        _activeFilter.selectedRank = null
        _activeFilter.selectedWorld = null
        _items.value = _forces
    }

    fun onInteractionCompleted() {
        _interaction.value = null
    }
}
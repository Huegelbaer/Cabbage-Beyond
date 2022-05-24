package com.cabbagebeyond.ui.collection.forces

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.cabbagebeyond.R
import com.cabbagebeyond.data.ForceDataSource
import com.cabbagebeyond.model.Force
import com.cabbagebeyond.model.World
import com.cabbagebeyond.ui.collection.CollectionListViewModel
import kotlinx.coroutines.launch

class ForcesViewModel(
    application: Application,
    private val forceDataSource: ForceDataSource
) : CollectionListViewModel(application) {

    object Filter {
        var selectedRank: ForceRank? = null
        var selectedWorld: World? = null

        fun reset() {
            selectedRank = null
            selectedRank = null
        }
    }

    sealed class Interaction {
        data class OpenFilter(val ranks: FilterData<ForceRank>, val worlds: FilterData<World>) : Interaction()
    }

    data class EmptyListState(val title: String, val message: String, val button: String?, val action: (() -> Unit)?)

    private var _forces = listOf<Force>()
    private var _items = MutableLiveData<List<Force>>()
    val items: LiveData<List<Force>>
        get() = _items

    private var _emptyListState = MutableLiveData<EmptyListState>()
    val emptyListState: LiveData<EmptyListState>
        get() = _emptyListState

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
                val resources = getApplication<Application>()
                _emptyListState.value = EmptyListState(
                    resources.getString(R.string.empty_state_list_title),
                    resources.getString(R.string.empty_state_list_message),
                    resources.getString(R.string.empty_state_list_reset_button),
                null)
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
            _emptyListState.value = if (filteredItems.isEmpty()) {
                val result = listOf(rank?.title, world?.name).joinToString(" & ") { "'$it'" }
                val resources = getApplication<Application>().resources
                EmptyListState(
                    resources.getString(R.string.empty_state_filtered_list_title),
                    resources.getString(R.string.empty_state_filtered_list_message, result),
                    resources.getString(R.string.empty_state_filtered_list_reset_button)) {
                    resetFilter()
                }
            } else {
                null
            }
        }
    }

    private fun resetFilter() {
        _activeFilter.reset()
        _items.value = _forces
    }

    fun onInteractionCompleted() {
        _interaction.value = null
    }
}
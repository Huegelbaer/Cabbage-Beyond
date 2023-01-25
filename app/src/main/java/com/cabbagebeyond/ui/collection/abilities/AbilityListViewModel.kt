package com.cabbagebeyond.ui.collection.abilities

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.cabbagebeyond.R
import com.cabbagebeyond.data.AbilityDataSource
import com.cabbagebeyond.model.Ability
import com.cabbagebeyond.model.User
import com.cabbagebeyond.model.World
import com.cabbagebeyond.ui.collection.CollectionListViewModel
import kotlinx.coroutines.launch

class AbilityListViewModel(
    user: User,
    private val app: Application,
    private val abilityDataSource: AbilityDataSource
) : CollectionListViewModel<Ability>(user, app) {

    object Filter {
        var selectedAttribute: AbilityAttribute? = null
        var selectedWorld: World? = null
    }

    sealed class Interaction {
        data class OpenFilter(
            val attributes: FilterData<AbilityAttribute>,
            val worlds: FilterData<World>
        ) : Interaction()
    }

    private var _abilities: List<Ability> = listOf()

    private var _interaction = MutableLiveData<Interaction?>()
    val interaction: LiveData<Interaction?>
        get() = _interaction

    private var _activeFilter = Filter

    init {
        viewModelScope.launch {
            _abilities = abilityDataSource.getAbilities().getOrDefault(listOf())
            mutableItems.value = _abilities
            if (_abilities.isEmpty()) {
                showNoContentAvailable()
            }
        }
    }

    override fun onSelectFilter() {
        val application = getApplication<Application>()
        val attributes =
            _abilities.map { AbilityAttribute.create(it.attribute, app) }.toSet().toList()
        val worlds = _abilities.mapNotNull { it.world }.toSet().toList()

        _interaction.value = Interaction.OpenFilter(
            FilterData(
                application.resources.getString(R.string.attribute),
                attributes,
                _activeFilter.selectedAttribute,
                AbilityAttribute::title
            ),
            FilterData(
                application.resources.getString(R.string.character_world),
                worlds,
                _activeFilter.selectedWorld,
                World::name
            )
        )
    }

    fun filter(attribute: AbilityAttribute?, world: World?) {
        _activeFilter.selectedAttribute = attribute
        _activeFilter.selectedWorld = world

        viewModelScope.launch {
            val filteredItems = _abilities.filter { ability ->
                val iAttribute = attribute?.let { attribute ->
                    ability.attribute == attribute.attribute
                } ?: true
                val iWorld = world?.let { world ->
                    ability.world == world
                } ?: true

                iAttribute && iWorld
            }
            mutableItems.value = filteredItems
            if (filteredItems.isEmpty()) {
                val searchTerm = listOfNotNull(attribute?.title, world?.name)
                showNoFilterResult(searchTerm) {
                    resetFilter()
                }
            } else {
                resetEmptyState()
            }
        }
    }

    private fun resetFilter() {
        _activeFilter.selectedAttribute = null
        _activeFilter.selectedWorld = null
        mutableItems.value = _abilities
    }

    fun onInteractionCompleted() {
        _interaction.value = null
    }
}
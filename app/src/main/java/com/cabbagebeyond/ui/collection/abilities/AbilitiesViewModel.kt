package com.cabbagebeyond.ui.collection.abilities

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.cabbagebeyond.R
import com.cabbagebeyond.data.AbilityDataSource
import com.cabbagebeyond.model.Ability
import com.cabbagebeyond.model.World
import com.cabbagebeyond.ui.collection.CollectionListViewModel
import kotlinx.coroutines.launch

class AbilitiesViewModel(
    private val app: Application,
    private val abilityDataSource: AbilityDataSource
) : CollectionListViewModel(app) {

    object Filter {
        var selectedAttribute: AbilityAttribute? = null
        var selectedWorld: World? = null
    }

    sealed class Interaction {
        data class OpenFilter(val attributes: FilterData<AbilityAttribute>, val worlds: FilterData<World>) : Interaction()
    }

    private var _abilities: List<Ability> = listOf()
    private var _items = MutableLiveData<List<Ability>>()
    val items: LiveData<List<Ability>>
        get() = _items

    private var _selectedAbility = MutableLiveData<Ability?>()
    val selectedAbility: LiveData<Ability?>
        get() = _selectedAbility

    private var _interaction = MutableLiveData<Interaction?>()
    val interaction: LiveData<Interaction?>
        get() = _interaction

    private var _activeFilter = Filter

    init {
        viewModelScope.launch {
            _abilities = abilityDataSource.getAbilities().getOrDefault(listOf())
            _items.value = _abilities
        }
    }

    fun onAbilityClicked(ability: Ability) {
        _selectedAbility.value = ability
    }

    fun onNavigationCompleted() {
        _selectedAbility.value = null
    }


    override fun onSelectFilter() {
        val application = getApplication<Application>()
        val attributes = _abilities.map { AbilityAttribute.create(it.attribute, app) }.toSet().toList()
        val worlds = _abilities.mapNotNull { it.world }.toSet().toList()

        _interaction.value = Interaction.OpenFilter(
            FilterData(application.resources.getString(R.string.attribute), attributes, _activeFilter.selectedAttribute, AbilityAttribute::title),
            FilterData(application.resources.getString(R.string.character_world), worlds, _activeFilter.selectedWorld, World::name)
        )
    }

    fun filter(attribute: AbilityAttribute?, world: World?) {
        _activeFilter.selectedAttribute = attribute
        _activeFilter.selectedWorld = world

        viewModelScope.launch {
            _items.value = _abilities.filter { ability ->
                val iAttribute = attribute?.let { attribute ->
                     ability.attribute == attribute.attribute
                } ?: true
                val iWorld = world?.let { world ->
                    ability.world == world
                } ?: true

                iAttribute && iWorld
            }
        }
    }

    fun onInteractionCompleted() {
        _interaction.value = null
    }
}
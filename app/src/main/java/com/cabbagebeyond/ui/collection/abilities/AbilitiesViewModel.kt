package com.cabbagebeyond.ui.collection.abilities

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cabbagebeyond.data.AbilityDataSource
import com.cabbagebeyond.model.Ability
import kotlinx.coroutines.launch

class AbilitiesViewModel(
    private val abilityDataSource: AbilityDataSource
) : ViewModel() {

    private var _items = MutableLiveData<List<Ability>>()
    val items: LiveData<List<Ability>>
        get() = _items

    private var _selectedAbility = MutableLiveData<Ability?>()
    val selectedAbility: LiveData<Ability?>
        get() = _selectedAbility

    init {
        viewModelScope.launch {
            _items.value = abilityDataSource.getAbilities().getOrDefault(listOf())
        }
    }

    fun onAbilityClicked(ability: Ability) {
        _selectedAbility.value = ability
    }

    fun onNavigationCompleted() {
        _selectedAbility.value = null
    }
}
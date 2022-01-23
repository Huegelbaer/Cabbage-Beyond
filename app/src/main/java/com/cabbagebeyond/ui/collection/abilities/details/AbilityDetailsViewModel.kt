package com.cabbagebeyond.ui.collection.abilities.details

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.cabbagebeyond.R
import com.cabbagebeyond.data.AbilityDataSource
import com.cabbagebeyond.data.WorldDataSource
import com.cabbagebeyond.model.Ability
import com.cabbagebeyond.model.User
import com.cabbagebeyond.model.World
import com.cabbagebeyond.ui.DetailsViewModel
import kotlinx.coroutines.launch

class AbilityDetailsViewModel(
    givenAbility: Ability,
    private val _abilityDataSource: AbilityDataSource,
    private val _worldDataSource: WorldDataSource,
    user: User,
    app: Application
) : DetailsViewModel(user, app) {

    var ability = MutableLiveData(givenAbility)

    private var _worlds = MutableLiveData<List<World?>>()
    val worlds: LiveData<List<World?>>
        get() = _worlds

    private var _attributes = MutableLiveData<List<String>>()
    val attributes: LiveData<List<String>>
        get() = _attributes

    init {
        // for MVP the attributes are stored in resources.
        val stringArray = app.applicationContext.resources.getStringArray(R.array.attributes)
        _attributes.value = stringArray.toList()
    }

    override fun onEdit() {
        super.onEdit()
        if (_worlds.value == null) {
            loadWorlds()
        }
        if (_attributes.value == null) {
            loadAttributes()
        }
    }

    private fun loadWorlds() {
        viewModelScope.launch {
            val worlds: MutableList<World?> = _worldDataSource.getWorlds().getOrDefault(listOf()).toMutableList()
            worlds.add(0, null)
            _worlds.value = worlds
        }
    }

    private fun loadAttributes() {

    }

    override fun onSave() {
        super.onSave()
        ability.value?.let {
            save(it)
        }
    }

    private fun save(toSafe: Ability) {
        viewModelScope.launch {
            val result = _abilityDataSource.saveAbility(toSafe)
            if (result.isSuccess) {
                message.value = R.string.save_completed
                ability.value = toSafe
            } else {
                message.value = R.string.save_failed
            }
        }
    }

    fun onAttributeSelected(attribute: String?) {
        val givenAbility = ability.value
        givenAbility?.attribute = attribute ?: ""
        ability.value = givenAbility
    }

    fun onWorldSelected(world: World?) {
        val givenAbility = ability.value
        givenAbility?.world = world
        ability.value = givenAbility
    }
}
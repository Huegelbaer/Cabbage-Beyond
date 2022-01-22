package com.cabbagebeyond.ui.collection.abilities.details

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cabbagebeyond.R
import com.cabbagebeyond.data.AbilityDataSource
import com.cabbagebeyond.data.WorldDataSource
import com.cabbagebeyond.model.Ability
import com.cabbagebeyond.model.User
import com.cabbagebeyond.model.World
import com.cabbagebeyond.util.Feature
import kotlinx.coroutines.launch

class AbilityDetailsViewModel(
    givenAbility: Ability,
    private val _abilityDataSource: AbilityDataSource,
    private val _worldDataSource: WorldDataSource,
    user: User,
    context: Context
) : ViewModel() {

    val userCanEdit = user.features.contains(Feature.CONFIGURE_APP.name)

    private var _isEditing = MutableLiveData(false)
    val isEditing: LiveData<Boolean>
        get() = _isEditing

    var ability = MutableLiveData(givenAbility)

    private var _fabImage = MutableLiveData(R.drawable.ic_edit)
    val fabImage: LiveData<Int>
        get() = _fabImage

    private var _worlds = MutableLiveData<List<World?>>()
    val worlds: LiveData<List<World?>>
        get() = _worlds

    private var _attributes = MutableLiveData<List<String>>()
    val attributes: LiveData<List<String>>
        get() = _attributes

    private var _message = MutableLiveData<Int?>()
    val message: LiveData<Int?>
        get() = _message

    init {
        // for MVP the attributes are stored in resources.
        val stringArray = context.resources.getStringArray(R.array.attributes)
        _attributes.value = stringArray.toList()
    }

    fun onClickFab() {
        val inEditMode = _isEditing.value ?: false

        if (inEditMode) {
            onSave()
        } else {
            onEdit()
        }
        _isEditing.value = !inEditMode
    }

    private fun onEdit() {
        _fabImage.value = R.drawable.ic_save
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

    private fun onSave() {
        ability.value?.let {
            save(it)
        }
        _fabImage.value = R.drawable.ic_edit
    }

    private fun save(toSafe: Ability) {
        viewModelScope.launch {
            val result = _abilityDataSource.saveAbility(toSafe)
            if (result.isSuccess) {
                _message.value = R.string.save_completed
                ability.value = toSafe
            } else {
                _message.value = R.string.save_failed
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
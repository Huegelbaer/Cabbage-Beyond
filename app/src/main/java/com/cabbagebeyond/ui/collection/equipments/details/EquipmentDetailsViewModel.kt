package com.cabbagebeyond.ui.collection.equipments.details

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cabbagebeyond.R
import com.cabbagebeyond.data.EquipmentDataSource
import com.cabbagebeyond.data.WorldDataSource
import com.cabbagebeyond.model.Ability
import com.cabbagebeyond.model.Equipment
import com.cabbagebeyond.model.User
import com.cabbagebeyond.model.World
import com.cabbagebeyond.util.Feature
import kotlinx.coroutines.launch

class EquipmentDetailsViewModel(
    givenEquipment: Equipment,
    private val _equipmentDataSource: EquipmentDataSource,
    private val _worldDataSource: WorldDataSource,
    user: User,
    context: Context
) : ViewModel() {

    val userCanEdit = user.features.contains(Feature.CONFIGURE_APP.name)

    private var _isEditing = MutableLiveData(false)
    val isEditing: LiveData<Boolean>
        get() = _isEditing

    var equipment = MutableLiveData(givenEquipment)

    private var _fabImage = MutableLiveData(R.drawable.ic_edit)
    val fabImage: LiveData<Int>
        get() = _fabImage

    private var _worlds = MutableLiveData<List<World?>>()
    val worlds: LiveData<List<World?>>
        get() = _worlds

    private var _types = MutableLiveData<List<String>>()
    val types: LiveData<List<String>>
        get() = _types

    private var _message = MutableLiveData<Int?>()
    val message: LiveData<Int?>
        get() = _message

    init {
        // for MVP the types are stored in resources.
        val stringArray = context.resources.getStringArray(R.array.attributes)
        _types.value = stringArray.toList()
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
        if (_types.value == null) {
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
        equipment.value?.let {
            save(it)
        }
        _fabImage.value = R.drawable.ic_edit
    }

    private fun save(equipment: Equipment) {
        viewModelScope.launch {
            val result = _equipmentDataSource.saveEquipment(equipment)
            _message.value = if (result.isSuccess) {
                R.string.save_completed
            } else {
                R.string.save_failed
            }
        }
    }

    fun onAttributeSelected(attribute: String?) {
       /* val givenAbility = ability.value
        givenAbility?.attribute = attribute ?: ""
        ability.value = givenAbility*/
    }

    fun onWorldSelected(world: World?) {
        val given = equipment.value
        given?.world = world
        equipment.value = given
    }
}
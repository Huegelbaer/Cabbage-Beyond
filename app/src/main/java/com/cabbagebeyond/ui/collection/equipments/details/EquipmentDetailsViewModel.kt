package com.cabbagebeyond.ui.collection.equipments.details

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.cabbagebeyond.R
import com.cabbagebeyond.data.EquipmentDataSource
import com.cabbagebeyond.data.WorldDataSource
import com.cabbagebeyond.model.Equipment
import com.cabbagebeyond.model.User
import com.cabbagebeyond.model.World
import com.cabbagebeyond.ui.DetailsViewModel
import kotlinx.coroutines.launch

class EquipmentDetailsViewModel(
    givenEquipment: Equipment,
    private val _equipmentDataSource: EquipmentDataSource,
    private val _worldDataSource: WorldDataSource,
    user: User,
    app: Application
) : DetailsViewModel(user, app) {

    var equipment = MutableLiveData(givenEquipment)

    private var _worlds = MutableLiveData<List<World?>>()
    val worlds: LiveData<List<World?>>
        get() = _worlds

    private var _types = MutableLiveData<List<String>>()
    val types: LiveData<List<String>>
        get() = _types

    init {
        // for MVP the types are stored in resources.
        val stringArray = app.applicationContext.resources.getStringArray(R.array.types)
        _types.value = stringArray.toList()
    }

    override fun onEdit() {
        super.onEdit()
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

    override fun onSave() {
        super.onSave()
        equipment.value?.let {
            save(it)
        }
    }

    private fun save(toSafe: Equipment) {
        viewModelScope.launch {
            val result = _equipmentDataSource.saveEquipment(toSafe)
            if (result.isSuccess) {
                message.value = R.string.save_completed
                equipment.value = toSafe
            } else {
                message.value = R.string.save_failed
            }
        }
    }

    fun onTypeSelected(type: String) {
        equipment.value?.type = type
    }

    fun onWorldSelected(world: World?) {
        val given = equipment.value
        given?.world = world
        equipment.value = given
    }
}
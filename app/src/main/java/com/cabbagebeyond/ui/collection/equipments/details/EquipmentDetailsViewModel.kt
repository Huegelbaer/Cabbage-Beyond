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
import com.cabbagebeyond.ui.collection.equipments.EquipmentType
import com.cabbagebeyond.util.CollectionProperty
import kotlinx.coroutines.launch

class EquipmentDetailsViewModel(
    givenEquipment: Equipment,
    isEditingActive: Boolean,
    private val _equipmentDataSource: EquipmentDataSource,
    private val _worldDataSource: WorldDataSource,
    user: User,
    app: Application
) : DetailsViewModel(user, isEditingActive, app) {

    data class TypeSelection(var selected: EquipmentType?, var values: List<EquipmentType>)
    data class WorldSelection(var selected: World?, var values: List<World?>)

    var equipment = MutableLiveData(givenEquipment)

    private var _worlds = MutableLiveData<WorldSelection>()
    val worlds: LiveData<WorldSelection>
        get() = _worlds

    private var _types = MutableLiveData<TypeSelection>()
    val types: LiveData<TypeSelection>
        get() = _types

    init {
        properties = arrayOf(
            CollectionProperty("name", R.string.character_name, ""),
            CollectionProperty("type", R.string.character_type, ""),
            CollectionProperty("cost", R.string.cost, ""),
            CollectionProperty("weight", R.string.weight, ""),
            CollectionProperty("requirements", R.string.requirement_title, ""),
            CollectionProperty("description", R.string.character_description, "")
        )
    }

    override fun onEdit() {
        super.onEdit()

        _types.value?.values?.let { updateTypeSelection(it) } ?: loadTypes()
        _worlds.value?.values?.let { updateWorldSelection(it) } ?: loadWorlds()

    }

    private fun loadTypes() {
        val application = getApplication<Application>()
        val attributes = Equipment.Type.values().map { EquipmentType.create(it, application) }
        updateTypeSelection(attributes)
    }

    private fun updateTypeSelection(types: List<EquipmentType>) {
        val application = getApplication<Application>()
        val currentSelected = equipment.value?.type?.let { EquipmentType.create(it, application) }
        _types.value = TypeSelection(currentSelected, types)
    }

    private fun loadWorlds() {
        viewModelScope.launch {
            val worlds: MutableList<World?> = _worldDataSource.getWorlds().getOrDefault(listOf()).toMutableList()
            worlds.add(0, null)
            updateWorldSelection(worlds)
        }
    }

    private fun updateWorldSelection(worlds: List<World?>) {
        _worlds.value = WorldSelection(equipment.value?.world, worlds)
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

    fun onTypeSelected(type: EquipmentType) {
        equipment.value?.type = type.type
    }

    fun onWorldSelected(world: World?) {
        val given = equipment.value
        given?.world = world
        equipment.value = given
    }

    override fun onPropertiesReceived(properties: Array<CollectionProperty>) {
        for (property in properties) {
            when (property.key) {
                "name" -> equipment.value?.name = property.value
         //       "type" -> equipment.value?.type = property.value
                "cost" -> equipment.value?.cost = property.value
                "weight" -> equipment.value?.weight = property.value.toDouble()
                "requirements" -> equipment.value?.requirements = property.value.split(", ")
                "description" -> equipment.value?.description += property.value
            }
        }
    }
}
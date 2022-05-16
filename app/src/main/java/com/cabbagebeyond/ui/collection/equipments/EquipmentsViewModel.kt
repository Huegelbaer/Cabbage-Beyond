package com.cabbagebeyond.ui.collection.equipments

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.cabbagebeyond.R
import com.cabbagebeyond.data.EquipmentDataSource
import com.cabbagebeyond.model.Equipment
import com.cabbagebeyond.model.World
import com.cabbagebeyond.ui.collection.CollectionListViewModel
import kotlinx.coroutines.launch

class EquipmentsViewModel(
    application: Application,
    private val equipmentDataSource: EquipmentDataSource
) : CollectionListViewModel(application) {

    object Filter {
        var selectedType: EquipmentType? = null
        var selectedWorld: World? = null
    }

    sealed class Interaction {
        data class OpenFilter(val types: FilterData<EquipmentType>, val worlds: FilterData<World>) : Interaction()
    }

    private var _equipments = listOf<Equipment>()
    private var _items = MutableLiveData<List<Equipment>>()
    val items: LiveData<List<Equipment>>
        get() = _items

    private var _selectedEquipment = MutableLiveData<Equipment?>()
    val selectedEquipment: LiveData<Equipment?>
        get() = _selectedEquipment

    private var _interaction = MutableLiveData<Interaction?>()
    val interaction: LiveData<Interaction?>
        get() = _interaction

    private var _activeFilter = Filter

    init {
        viewModelScope.launch {
            _equipments = equipmentDataSource.getEquipments().getOrDefault(listOf())
            _items.value = _equipments
        }
    }


    fun onEquipmentClicked(equipment: Equipment) {
        _selectedEquipment.value = equipment
    }

    fun onNavigationCompleted() {
        _selectedEquipment.value = null
    }

    override fun onSelectFilter() {
        val application = getApplication<Application>()
        val types = _equipments.mapNotNull { equipment -> equipment.type?.let { EquipmentType.create(it, application) } }.toSet().toList()
        val worlds = _equipments.mapNotNull { it.world }.toSet().toList()

        _interaction.value = Interaction.OpenFilter(
            FilterData(application.resources.getString(R.string.character_type), types, _activeFilter.selectedType, EquipmentType::title),
            FilterData(application.resources.getString(R.string.character_world), worlds, _activeFilter.selectedWorld, World::name)
        )
    }

    fun filter(type: EquipmentType?, world: World?) {
        _activeFilter.selectedType = type
        _activeFilter.selectedWorld = world

        viewModelScope.launch {
            _items.value = _equipments.filter { equipment ->
                val iType = type?.let { type ->
                    equipment.type == type.type
                } ?: true
                val iWorld = world?.let { world ->
                    equipment.world == world
                } ?: true

                iType && iWorld
            }
        }
    }

    fun onInteractionCompleted() {
        _interaction.value = null
    }
}
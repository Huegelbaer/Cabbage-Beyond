package com.cabbagebeyond.ui.collection.equipments

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cabbagebeyond.data.EquipmentDataSource
import com.cabbagebeyond.model.Equipment
import kotlinx.coroutines.launch

class EquipmentsViewModel(
    private val equipmentDataSource: EquipmentDataSource
) : ViewModel() {

    private var _items = MutableLiveData<List<Equipment>>()
    val items: LiveData<List<Equipment>>
        get() = _items

    private var _selectedEquipment = MutableLiveData<Equipment?>()
    val selectedEquipment: LiveData<Equipment?>
        get() = _selectedEquipment

    init {
        viewModelScope.launch {
            _items.value = equipmentDataSource.getEquipments().getOrDefault(listOf())
        }
    }

    fun onEquipmentClicked(equipment: Equipment) {
        _selectedEquipment.value = equipment
    }

    fun onNavigationCompleted() {
        _selectedEquipment.value = null
    }
}
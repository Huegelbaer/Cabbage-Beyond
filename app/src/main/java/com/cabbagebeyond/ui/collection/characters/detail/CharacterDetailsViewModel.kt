package com.cabbagebeyond.ui.collection.characters.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cabbagebeyond.R
import com.cabbagebeyond.data.*
import com.cabbagebeyond.data.dao.*
import com.cabbagebeyond.data.repository.*
import com.cabbagebeyond.model.Character
import kotlinx.coroutines.launch

class CharacterDetailsViewModel(
    private val character: Character,
    private val characterDataSource: CharacterDataSource,
    private val talentDataSource: TalentDataSource,
    private val handicapDataSource: HandicapDataSource,
    private val forceDataSource: ForceDataSource,
    private val equipmentDataSource: EquipmentDataSource
    ) : ViewModel() {

    sealed class Item(val title: String)
    class HeaderItem(title: String, val icon: Int, var items: MutableList<ListItem>): Item(title)
    class ListItem(title: String, var content: Any): Item(title)

    private var _items = MutableLiveData<MutableList<Item>>()
    val items: LiveData<MutableList<Item>>
        get() = _items

    init {

        viewModelScope.launch {
            val itemList = mutableListOf<Item>()
            val talents = talentDataSource.getTalents(character.talents).getOrDefault(listOf()).map {
                ListItem(it.name, it)
            }.toMutableList()
            val talentItem = HeaderItem("Talents", R.drawable.ic_thumb_up, talents)
            itemList.add(talentItem)

            val handicaps = handicapDataSource.getHandicaps(character.handicaps).getOrDefault(listOf()).map {
                ListItem(it.name, it)
            }.toMutableList()
            val handicapItem = HeaderItem("Handicaps", R.drawable.ic_thumb_down, handicaps)
            itemList.add(handicapItem)

            val forces = forceDataSource.getForces(character.forces).getOrDefault(listOf()).map {
                ListItem(it.name, it)
            }.toMutableList()
            val forceItem = HeaderItem("Forces", R.drawable.ic_local_library, forces)
            itemList.add(forceItem)

            val equipments = equipmentDataSource.getEquipments(character.equipments).getOrDefault(listOf()).map {
                ListItem(it.name, it)
            }.toMutableList()
            val equipmentItem = HeaderItem("Equipments", R.drawable.ic_security, equipments)
            itemList.add(equipmentItem)

            _items.value = itemList
        }
    }

    fun expandHeader(headerItem: HeaderItem) {
        val list = _items.value ?: return
        val position = list.indexOf(headerItem) + 1
        list.addAll(position, headerItem.items)
        _items.value = list
    }

    fun collapseHeader(headerItem: HeaderItem) {
        val list = _items.value ?: return
        list.removeAll(headerItem.items)
        _items.value = list
    }

    fun show(listItem: ListItem) {

    }
}
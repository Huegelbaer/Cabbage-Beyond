package com.cabbagebeyond.ui.collection.characters.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.cabbagebeyond.R
import com.cabbagebeyond.model.Character

class CharacterDetailsViewModel(private val character: Character) : ViewModel() {

    sealed class Item(val title: String)
    class HeaderItem(title: String, val icon: Int, var items: MutableList<ListItem>): Item(title)
    class ListItem(title: String, var content: Any): Item(title)

    private var _items = MutableLiveData<MutableList<Item>>()
    val items: LiveData<MutableList<Item>>
        get() = _items

    init {
        val itemList = mutableListOf<Item>()

        val talents = character.talents.map {
                ListItem(it.name, it)
            }.toMutableList()
        val talentItem = HeaderItem("Talents", R.drawable.ic_thumb_up, talents)
        itemList.add(talentItem)

        val handicaps = character.handicaps.map {
            ListItem(it.name, it)
        }.toMutableList()
        val handicapItem = HeaderItem("Handicaps", R.drawable.ic_thumb_down, handicaps)
        itemList.add(handicapItem)

        val forces = character.forces.map {
            ListItem(it.name, it)
        }.toMutableList()
        val forceItem = HeaderItem("Forces", R.drawable.ic_local_library, forces)
        itemList.add(forceItem)

        val equipments = character.equipments.map {
            ListItem(it.name, it)
        }.toMutableList()
        val equipmentItem = HeaderItem("Equipments", R.drawable.ic_security, equipments)
        itemList.add(equipmentItem)

        _items.value = itemList
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
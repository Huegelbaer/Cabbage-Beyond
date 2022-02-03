package com.cabbagebeyond.ui.collection.characters.details

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.cabbagebeyond.R
import com.cabbagebeyond.data.CharacterDataSource
import com.cabbagebeyond.model.Character
import com.cabbagebeyond.model.User
import com.cabbagebeyond.ui.DetailsViewModel
import com.cabbagebeyond.util.CollectionProperty
import kotlinx.coroutines.launch

class CharacterDetailsViewModel(
    givenCharacter: Character,
    private val _characterDataSource: CharacterDataSource,
    user: User,
    app: Application
) : DetailsViewModel(user, app) {

    sealed class Item(val title: String)
    class HeaderItem(title: String, val icon: Int, var items: MutableList<ListItem>): Item(title)
    class ListItem(title: String, var content: Any): Item(title)

    var character = MutableLiveData(givenCharacter)

    private var _items = MutableLiveData<MutableList<Item>>()
    val items: LiveData<MutableList<Item>>
        get() = _items

    init {
        val itemList = mutableListOf<Item>()

        val talents = givenCharacter.talents.map {
                ListItem(it.name, it)
            }.toMutableList()
        val talentItem = HeaderItem("Talents", R.drawable.ic_thumb_up, talents)
        itemList.add(talentItem)

        val handicaps = givenCharacter.handicaps.map {
            ListItem(it.name, it)
        }.toMutableList()
        val handicapItem = HeaderItem("Handicaps", R.drawable.ic_thumb_down, handicaps)
        itemList.add(handicapItem)

        val forces = givenCharacter.forces.map {
            ListItem(it.name, it)
        }.toMutableList()
        val forceItem = HeaderItem("Forces", R.drawable.ic_local_library, forces)
        itemList.add(forceItem)

        val equipments = givenCharacter.equipments.map {
            ListItem(it.name, it)
        }.toMutableList()
        val equipmentItem = HeaderItem("Equipments", R.drawable.ic_security, equipments)
        itemList.add(equipmentItem)

        _items.value = itemList

        properties = arrayOf(
            CollectionProperty("name", R.string.character_name, ""),
            CollectionProperty("description", R.string.character_description, "")
        )
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

    override fun onSave() {
        super.onSave()
        character.value?.let {
            save(it)
        }
    }

    private fun save(toSafe: Character) {
        viewModelScope.launch {
            val result = _characterDataSource.saveCharacter(toSafe)
            if (result.isSuccess) {
                message.value = R.string.save_completed
                character.value = toSafe
            } else {
                message.value = R.string.save_failed
            }
        }
    }

    fun show(listItem: ListItem) {

    }

    override fun onPropertiesReceived(properties: Array<CollectionProperty>) {
        for (property in properties) {
            when (property.key) {
                "name" -> character.value?.name = property.value
                "description" -> character.value?.description += property.value
            }
        }
    }
}
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
        val itemList = mutableListOf<Item>(
            extractTalents(givenCharacter),
            extractHandicaps(givenCharacter),
            extractAbilities(givenCharacter),
            extractForces(givenCharacter),
            extractEquipments(givenCharacter)
        )

        _items.value = itemList
    }

    private fun extractTalents(character: Character): HeaderItem {
        val talents = character.talents.map {
            ListItem(it.name, it)
        }.toMutableList()
        return HeaderItem("Talents", R.drawable.ic_thumb_up, talents)
    }

    private fun extractHandicaps(character: Character): HeaderItem {
        val handicaps = character.handicaps.map {
            ListItem(it.name, it)
        }.toMutableList()
        return HeaderItem("Handicaps", R.drawable.ic_thumb_down, handicaps)
    }

    private fun extractAbilities(character: Character): HeaderItem {
        val abilities = character.abilities.map {
            ListItem(it.name, it)
        }.toMutableList()
        return HeaderItem("Abilities", R.drawable.ic_menu_abilities, abilities)
    }

    private fun extractForces(character: Character): HeaderItem {
        val forces = character.forces.map {
            ListItem(it.name, it)
        }.toMutableList()
        return HeaderItem("Forces", R.drawable.ic_local_library, forces)
    }

    private fun extractEquipments(character: Character): HeaderItem {
        val equipments = character.equipments.map {
            ListItem(it.name, it)
        }.toMutableList()
        return HeaderItem("Equipments", R.drawable.ic_security, equipments)
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
}
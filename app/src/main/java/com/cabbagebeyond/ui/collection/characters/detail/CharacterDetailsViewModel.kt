package com.cabbagebeyond.ui.collection.characters.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cabbagebeyond.R
import com.cabbagebeyond.data.dao.*
import com.cabbagebeyond.data.repository.*
import com.cabbagebeyond.model.Character
import kotlinx.coroutines.launch

class CharacterDetailsViewModel(val character: Character) : ViewModel() {

    sealed class Item(val title: String)
    class HeaderItem(title: String, val icon: Int, var items: MutableList<ListItem>): Item(title)
    class ListItem(title: String, var content: Any): Item(title)

    private lateinit var userRepository: UserRepository
    private lateinit var characterRepository: CharacterRepository
    private lateinit var talentRepository: TalentRepository
    private lateinit var handicapRepository: HandicapRepository
    private lateinit var forceRepository: ForceRepository
    private lateinit var equipmentRepository: EquipmentRepository

    private var _items = MutableLiveData<MutableList<Item>>()
    val items: LiveData<MutableList<Item>>
        get() = _items

    init {
        userRepository = UserRepository(UserDao())
        characterRepository = CharacterRepository(CharacterDao())
        talentRepository = TalentRepository(TalentDao())
        handicapRepository = HandicapRepository(HandicapDao())
        forceRepository = ForceRepository(ForceDao())
        equipmentRepository = EquipmentRepository(EquipmentDao())

        viewModelScope.launch {
            val itemList = mutableListOf<Item>()
            val talents = talentRepository.getTalents().getOrDefault(listOf()).map {
                ListItem(it.name, it)
            }.toMutableList()
            val talentItem = HeaderItem("Talents", R.drawable.ic_thumb_up, talents)
            itemList.add(talentItem)

            val handicaps = handicapRepository.getHandicaps().getOrDefault(listOf()).map {
                ListItem(it.name, it)
            }.toMutableList()
            val handicapItem = HeaderItem("Handicaps", R.drawable.ic_thumb_down, handicaps)
            itemList.add(handicapItem)

            val forces = forceRepository.getForces().getOrDefault(listOf()).map {
                ListItem(it.name, it)
            }.toMutableList()
            val forceItem = HeaderItem("Forces", R.drawable.ic_local_library, forces)
            itemList.add(forceItem)

            val equipments = equipmentRepository.getEquipments().getOrDefault(listOf()).map {
                ListItem(it.name, it)
            }.toMutableList()
            val equipmentItem = HeaderItem("Equipments", R.drawable.ic_security, equipments)
            itemList.add(equipmentItem)

            _items.value = itemList
        }
    }
}
package com.cabbagebeyond.ui.collection.characters.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cabbagebeyond.R
import com.cabbagebeyond.data.dao.*
import com.cabbagebeyond.data.repository.*
import com.cabbagebeyond.model.Character
import com.cabbagebeyond.model.Equipment
import com.cabbagebeyond.model.Force
import com.cabbagebeyond.model.Handicap
import com.cabbagebeyond.model.Talent
import kotlinx.coroutines.launch

class CharacterDetailsViewModel(val character: Character) : ViewModel() {

    sealed class CharacterDetailsItem(val title: String, val icon: Int, var items: MutableList<*>)

    class TalentItem(title: String, icon: Int, items: MutableList<Talent>):
        CharacterDetailsItem(title, icon, items)

    class HandicapItem(title: String, icon: Int, items: MutableList<Handicap>):
        CharacterDetailsItem(title, icon, items)

    class ForceItem(title: String, icon: Int, items: MutableList<Force>):
        CharacterDetailsItem(title, icon, items)

    class EquipmentItem(title: String, icon: Int, items: MutableList<Equipment>):
        CharacterDetailsItem(title, icon, items)

    private lateinit var userRepository: UserRepository
    private lateinit var characterRepository: CharacterRepository
    private lateinit var talentRepository: TalentRepository
    private lateinit var handicapRepository: HandicapRepository
    private lateinit var forceRepository: ForceRepository
    private lateinit var equipmentRepository: EquipmentRepository

    private var _items = MutableLiveData<MutableList<CharacterDetailsItem>>()
    val items: LiveData<MutableList<CharacterDetailsItem>>
        get() = _items

    init {
        userRepository = UserRepository(UserDao())
        characterRepository = CharacterRepository(CharacterDao())
        talentRepository = TalentRepository(TalentDao())
        handicapRepository = HandicapRepository(HandicapDao())
        forceRepository = ForceRepository(ForceDao())
        equipmentRepository = EquipmentRepository(EquipmentDao())

        viewModelScope.launch {
            val itemList = mutableListOf<CharacterDetailsItem>()
            val talents = talentRepository.getTalents().getOrDefault(listOf())
            val talentItem = TalentItem("Talents", R.drawable.ic_add, talents.toMutableList())
            itemList.add(talentItem)

            val handicaps = handicapRepository.getHandicaps().getOrDefault(listOf())
            val handicapItem = HandicapItem("Handicaps", R.drawable.ic_add, handicaps.toMutableList())
            itemList.add(handicapItem)

            val forces = forceRepository.getForces().getOrDefault(listOf())
            val forceItem = ForceItem("Forces", R.drawable.ic_add, forces.toMutableList())
            itemList.add(forceItem)

            val equipments = equipmentRepository.getEquipments().getOrDefault(listOf())
            val equipmentItem = EquipmentItem("Equipments", R.drawable.ic_add, equipments.toMutableList())
            itemList.add(equipmentItem)

            _items.value = itemList
        }
    }
}
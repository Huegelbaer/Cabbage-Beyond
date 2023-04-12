package com.cabbagebeyond.ui.collection.characters.details

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.cabbagebeyond.R
import com.cabbagebeyond.data.*
import com.cabbagebeyond.model.*
import com.cabbagebeyond.ui.DetailsViewModel
import com.cabbagebeyond.ui.collection.characters.CharacterType
import com.cabbagebeyond.util.CollectionProperty
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CharacterDetailsViewModel(
    givenCharacter: Character,
    isEditingActive: Boolean,
    private val _characterDataSource: CharacterDataSource,
    private val _raceDataSource: RaceDataSource,
    private val _worldDataSource: WorldDataSource,
    private val _talentDataSource: TalentDataSource,
    private val _handicapDataSource: HandicapDataSource,
    private val _forceDataSource: ForceDataSource,
    private val _equipmentDataSource: EquipmentDataSource,
    user: User,
    private val app: Application
) : DetailsViewModel(user, isEditingActive, app) {

    sealed class Item(val title: String)
    class HeaderItem(title: String, val icon: Int, var items: MutableList<ListItem>): Item(title)
    class ListItem(title: String, var content: Any): Item(title)

    var character = MutableLiveData(givenCharacter)

    private var _items = MutableLiveData<MutableList<Item>>()
    val items: LiveData<MutableList<Item>>
        get() = _items

    private var _worlds = MutableLiveData<NullableSelection<World>>()
    val worlds: LiveData<NullableSelection<World>>
        get() = _worlds

    private var _types = MutableLiveData<Selection<CharacterType>>()
    val types: LiveData<Selection<CharacterType>>
        get() = _types

    private var _races = MutableLiveData<Selection<Race>>()
    val races: LiveData<Selection<Race>>
        get() = _races

    private var _allTalents = MutableLiveData<List<Talent>>()
    val allTalents: LiveData<List<Talent>>
        get() = _allTalents

    private var _allHandicaps = MutableLiveData<List<Handicap>>()
    val allHandicaps: LiveData<List<Handicap>>
        get() = _allHandicaps

    private var _allForces = MutableLiveData<List<Force>>()
    val allForces: LiveData<List<Force>>
        get() = _allForces

    private var _allEquipments = MutableLiveData<List<Equipment>>()
    val allEquipments: LiveData<List<Equipment>>
        get() = _allEquipments

    init {
        updateItemList()

        properties = arrayOf(
            CollectionProperty("name", R.string.character_name, ""),
            CollectionProperty("description", R.string.character_description, "")
        )

        loadTypes()
        loadRaces()
        loadWorlds()

        loadAllTalents()
        loadAllHandicaps()
        loadAllForces()
        loadAllEquipments()
    }

    private fun updateItemList() {
        val character = character.value ?: return
        val resources = app.resources
        val itemList = mutableListOf<Item>()

        val talents = character.talents.map {
            ListItem(it.name, it)
        }.toMutableList()
        val talentItem = HeaderItem(
            resources.getString(R.string.character_details_talent_header, talents.size),
            R.drawable.ic_thumb_up, talents
        )
        itemList.add(talentItem)

        val handicaps = character.handicaps.map {
            ListItem(it.name, it)
        }.toMutableList()
        val handicapItem = HeaderItem(
            resources.getString(R.string.character_details_handicap_header, handicaps.size),
            R.drawable.ic_thumb_down, handicaps
        )
        itemList.add(handicapItem)

        val forces = character.forces.map {
            ListItem(it.name, it)
        }.toMutableList()
        val forceItem = HeaderItem(
            resources.getString(R.string.character_details_forces_header, forces.size),
            R.drawable.ic_local_library, forces
        )
        itemList.add(forceItem)

        val equipments = character.equipments.map {
            ListItem(it.name, it)
        }.toMutableList()
        val equipmentItem = HeaderItem(
            resources.getString(R.string.character_details_equipment_header, equipments.size),
            R.drawable.ic_security, equipments
        )
        itemList.add(equipmentItem)

        _items.value = itemList
    }

    private fun loadTypes() {
        val application = getApplication<Application>()
        val types = Character.Type.values().map { CharacterType.create(it, application) }
        val currentSelected = character.value?.type?.let { CharacterType.create(it, application) }
        _types.value = Selection(currentSelected, types)
    }

    private fun loadRaces() {
        viewModelScope.launch {
            val races = _raceDataSource.getRaces().getOrDefault(listOf()).toMutableList()
            withContext(Dispatchers.Main) {
                _races.value = Selection(character.value?.race, races)
            }
        }
    }
    private fun loadWorlds() {
        viewModelScope.launch {
            val worlds: MutableList<World?> = _worldDataSource.getWorlds().getOrDefault(listOf()).toMutableList()
            worlds.add(0, null)
            withContext(Dispatchers.Main) {
                _worlds.value = NullableSelection(character.value?.world, worlds)
            }
        }
    }

    private fun loadAllTalents() {
        viewModelScope.launch {
            val talents: List<Talent> = _talentDataSource.getTalents().getOrDefault(listOf())
            withContext(Dispatchers.Main) {
                _allTalents.value = talents
            }
        }
    }

    private fun loadAllHandicaps() {
        viewModelScope.launch {
            val handicaps: List<Handicap> = _handicapDataSource.getHandicaps().getOrDefault(listOf())
            withContext(Dispatchers.Main) {
                _allHandicaps.value = handicaps
            }
        }
    }

    private fun loadAllForces() {
        viewModelScope.launch {
            val forces: List<Force> = _forceDataSource.getForces().getOrDefault(listOf())
            withContext(Dispatchers.Main) {
                _allForces.value = forces
            }
        }
    }

    private fun loadAllEquipments() {
        viewModelScope.launch {
            val equipment: List<Equipment> = _equipmentDataSource.getEquipments().getOrDefault(listOf())
            withContext(Dispatchers.Main) {
                _allEquipments.value = equipment
            }
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

    fun updateTalents(selectedTalents: MutableList<Talent>) {
        character.value?.talents = selectedTalents
    }

    fun updateHandicaps(selectedHandicaps: MutableList<Handicap>) {
        character.value?.handicaps = selectedHandicaps
    }

    fun updateForces(selectedForces: MutableList<Force>) {
        character.value?.forces = selectedForces
    }

    fun updateEquipments(selectedEquipments: MutableList<Equipment>) {
        character.value?.equipments = selectedEquipments
    }

    override fun onSave() {
        super.onSave()
        character.value?.let {
            save(it)
        }
        updateItemList()

    }

    private fun save(toSafe: Character) {
        viewModelScope.launch {
            val result = _characterDataSource.saveCharacter(toSafe)
            if (result.isSuccess) {
                character.value = toSafe
                onSaveSucceeded()
            } else {
                onSaveFailed()
            }
        }
    }

    fun onRaceSelected(race: Race) {
        character.value?.race = race
    }

    fun onTypeSelected(type: CharacterType) {
        character.value?.type = type.type
    }

    fun onWorldSelected(world: World?) {
        character.value?.world = world
    }

    fun show(listItem: ListItem) {
        // TODO: Show list item
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
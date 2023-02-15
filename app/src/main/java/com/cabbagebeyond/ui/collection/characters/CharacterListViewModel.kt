package com.cabbagebeyond.ui.collection.characters

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.cabbagebeyond.R
import com.cabbagebeyond.data.CharacterDataSource
import com.cabbagebeyond.model.Character
import com.cabbagebeyond.model.Race
import com.cabbagebeyond.model.User
import com.cabbagebeyond.model.World
import com.cabbagebeyond.ui.collection.CollectionListViewModel
import kotlinx.coroutines.launch
import java.util.*

class CharacterListViewModel(
    user: User,
    private val app: Application,
    private val characterDataSource: CharacterDataSource
) : CollectionListViewModel<Character>(user, app) {

    enum class SortType {
        NAME, RACE, TYPE, WORLD, NONE
    }

    object Filter {
        var selectedRace: Race? = null
        var selectedType: CharacterType? = null
        var selectedWorld: World? = null
    }

    sealed class Interaction {
        data class OpenFilter(
            val races: FilterData<Race>,
            val types: FilterData<CharacterType>,
            val worlds: FilterData<World>
        ) : Interaction()
    }

    private var _activeFilter = Filter

    private var _interaction = MutableLiveData<Interaction?>()
    val interaction: LiveData<Interaction?>
        get() = _interaction

    private var _characters: List<Character> = listOf()

    init {
        viewModelScope.launch {
            _characters = characterDataSource.getCharacters().getOrDefault(listOf())
            mutableItems.value = _characters
            if (_characters.isEmpty()) {
                showNoContentAvailable()
            }
        }
    }

    override fun onSearch(query: String) {
        mutableItems.value = _characters.filter {
            it.name.contains(query) || it.description.contains(query)
        }
    }

    override fun onSearchCanceled() {
        mutableItems.value = _characters
    }

    fun onSelectSort(sortType: SortType) {
        viewModelScope.launch {
            val result = when (sortType) {
                SortType.NAME -> {
                    characterDataSource.getCharactersSortedByName()
                }
                SortType.RACE -> {
                    characterDataSource.getCharactersSortedByRace()
                }
                SortType.TYPE -> {
                    characterDataSource.getCharactersSortedByType()
                }
                SortType.WORLD -> {
                    characterDataSource.getCharactersSortedByWorld()
                }
                SortType.NONE -> {
                    characterDataSource.getCharactersSortedByName()
                }
            }
            _characters = result.getOrDefault(listOf())
            mutableItems.value = _characters
        }
    }

    override fun onSelectFilter() {
        val application = getApplication<Application>()
        val races = _characters.mapNotNull { it.race }.toSet().toList()
        val types = Character.Type.values().map {
            createCharacterType(it)
        }
        val worlds = _characters.mapNotNull { it.world }.toSet().toList()

        _interaction.value = Interaction.OpenFilter(
            FilterData(
                application.resources.getString(R.string.character_race),
                races,
                _activeFilter.selectedRace,
                Race::name
            ),
            FilterData(
                application.resources.getString(R.string.character_type),
                types,
                _activeFilter.selectedType,
                CharacterType::title
            ),
            FilterData(
                application.resources.getString(R.string.character_world),
                worlds,
                _activeFilter.selectedWorld,
                World::name
            )
        )
    }

    private fun createCharacterType(type: Character.Type): CharacterType {
        val titleId = when (type) {
            Character.Type.PLAYER -> R.string.character_type_player
            Character.Type.NPC -> R.string.character_type_npc
            Character.Type.MONSTER -> R.string.character_type_monster
        }
        val title = app.resources.getString(titleId)
        return CharacterType(type, title)
    }

    fun filter(race: Race?, characterType: CharacterType?, world: World?) {
        _activeFilter.selectedRace = race
        _activeFilter.selectedType = characterType
        _activeFilter.selectedWorld = world

        viewModelScope.launch {
            val filteredItems = _characters.filter { character ->
                val iRace = race?.let { race ->
                    character.race == race
                } ?: true
                val iType = characterType?.let { type ->
                    character.type == type.type
                } ?: true
                val iWorld = world?.let { world ->
                    character.world == world
                } ?: true

                iRace && iType && iWorld
            }
            mutableItems.value = filteredItems
            if (filteredItems.isEmpty()) {
                val searchTerm = listOfNotNull(race?.name, characterType?.title, world?.name)
                showNoFilterResult(searchTerm) {
                    resetFilter()
                }
            } else {
                resetEmptyState()
            }
        }
    }

    private fun resetFilter() {
        _activeFilter.selectedRace = null
        _activeFilter.selectedType = null
        _activeFilter.selectedWorld = null
        mutableItems.value = _characters
    }

    fun onInteractionCompleted() {
        _interaction.value = null
    }

    fun addCharacter() {
        val newEquipment = Character(
            "",
            null,
            "",
            0,
            "W4",
            "W4",
            "W4",
            "W4",
            "W4",
            "W4",
            "W4",
            "W4",
            "W4",
            0,
            0,
            "",
            listOf(),
            listOf(),
            listOf(),
            listOf(),
            listOf(),
            Character.Type.PLAYER,
            "",
            null,
            UUID.randomUUID().toString()
        )
        onCreateNewItem(newEquipment)
    }

}

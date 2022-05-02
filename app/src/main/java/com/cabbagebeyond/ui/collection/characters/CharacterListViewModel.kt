package com.cabbagebeyond.ui.collection.characters

import android.app.Application
import androidx.lifecycle.*
import com.cabbagebeyond.R
import com.cabbagebeyond.data.*
import com.cabbagebeyond.model.Character
import com.cabbagebeyond.model.Race
import com.cabbagebeyond.model.World
import com.cabbagebeyond.ui.collection.CollectionListViewModel
import kotlinx.coroutines.launch

class CharacterListViewModel(
    private val app: Application,
    private val characterDataSource: CharacterDataSource
) : CollectionListViewModel(app) {

    enum class SortType {
        NAME, RACE, TYPE, WORLD, NONE
    }

    class CharacterType(var type: Character.Type, var title: String)

    object Filter {
        var selectedRace: Race? = null
        var selectedType: CharacterType? = null
        var selectedWorld: World? = null
    }

    sealed class Interaction {
        data class OpenFilter(val races: FilterData<Race>, val types: FilterData<CharacterType>, val worlds: FilterData<World>) : Interaction()
    }

    private var _activeFilter = Filter

    private var _interaction = MutableLiveData<Interaction?>()
    val interaction: LiveData<Interaction?>
        get() = _interaction

    private var _characters: List<Character> = listOf()
    private var _items = MutableLiveData<List<Character>>()
    val items: LiveData<List<Character>>
        get() = _items

    private var _selectedCharacter = MutableLiveData<Character?>()
    val selectedCharacter: LiveData<Character?>
        get() = _selectedCharacter

    init {
        viewModelScope.launch {
            _characters = characterDataSource.getCharacters().getOrDefault(listOf())
            _items.value = _characters
        }
    }

    override fun onSearch(query: String) {
        _items.value = _characters.filter {
            it.name.contains(query) || it.description.contains(query)
        }
    }

    override fun onSearchCanceled() {
        _items.value = _characters
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
            _items.value = _characters
        }
    }

    override fun onSelectFilter() {
        val races = _characters.mapNotNull { it.race }.toSet().toList()
        val types = Character.Type.values().map {
            createCharacterType(it)
        }
        val worlds = _characters.mapNotNull { it.world }.toSet().toList()

        _interaction.value = Interaction.OpenFilter(
            FilterData(races, _activeFilter.selectedRace, Race::name),
            FilterData(types, _activeFilter.selectedType, CharacterType::title),
            FilterData(worlds, _activeFilter.selectedWorld, World::name)
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
            _items.value = _characters.filter { character ->
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
        }
    }

    fun onInteractionCompleted() {
        _interaction.value = null
    }

    fun onCharacterClicked(character: Character) {
        _selectedCharacter.value = character
    }

    fun onNavigationCompleted() {
        _selectedCharacter.value = null
    }
}

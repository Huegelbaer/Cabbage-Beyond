package com.cabbagebeyond.ui.collection.characters

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cabbagebeyond.data.*
import com.cabbagebeyond.model.Character
import com.cabbagebeyond.model.Race
import com.cabbagebeyond.model.World
import kotlinx.coroutines.launch

class CharacterListViewModel(
    private val characterDataSource: CharacterDataSource
) : ViewModel() {

    enum class SortType {
        NAME, RACE, TYPE, WORLD, NONE
    }

    object Filter {
        var selectedRace: Race? = null
        var selectedType: String? = null
        var selectedWorld: World? = null
    }

    sealed class Interaction {
        data class OpenFilter(val races: List<Race>, val types: List<String>, val worlds: List<World>, val selectedRace: Race?, val selectedType: String?, val selectedWorld: World?) : Interaction()
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

    fun onSearchCharacter(query: String) {
        _items.value = _characters.filter {
            it.name.contains(query) || it.description.contains(query)
        }
    }

    fun onSearchCanceled() {
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

    fun onSelectFilter() {
        val races = _characters.mapNotNull { it.race }.toSet().toList()
        val types = _characters.map { it.type }.toSet().toList()
        val worlds = _characters.mapNotNull { it.world }.toSet().toList()

        _interaction.value = Interaction.OpenFilter(races, types, worlds, _activeFilter.selectedRace, _activeFilter.selectedType, _activeFilter.selectedWorld)
    }

    fun filter(race: Race?, type: String?, world: World?) {
        _activeFilter.selectedRace = race
        _activeFilter.selectedType = type
        _activeFilter.selectedWorld = world

        viewModelScope.launch {
            _items.value = _characters.filter { character ->
                val iRace = race?.let { race ->
                    character.race == race
                } ?: true
                val iType = type?.let { type ->
                    character.type == type
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
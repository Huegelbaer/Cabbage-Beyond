package com.cabbagebeyond.ui.collection.characters

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cabbagebeyond.data.*
import com.cabbagebeyond.model.Character
import kotlinx.coroutines.launch

class CharacterListViewModel(
    private val characterDataSource: CharacterDataSource
) : ViewModel() {

    enum class SortType {
        NAME, RACE, TYPE, WORLD, NONE
    }

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

    fun onCharacterClicked(character: Character) {
        _selectedCharacter.value = character
    }

    fun onNavigationCompleted() {
        _selectedCharacter.value = null
    }
}
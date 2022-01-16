package com.cabbagebeyond.ui.collection.characters

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cabbagebeyond.R
import com.cabbagebeyond.data.*
import com.cabbagebeyond.model.Character
import kotlinx.coroutines.launch

class CharacterListViewModel(
    private val characterDataSource: CharacterDataSource
) : ViewModel() {

    private var _items = MutableLiveData<List<Character>>()
    val items: LiveData<List<Character>>
        get() = _items

    private var _selectedCharacter = MutableLiveData<Character?>()
    val selectedCharacter: LiveData<Character?>
        get() = _selectedCharacter

    init {
        viewModelScope.launch {
            _items.value = characterDataSource.getCharacters().getOrDefault(listOf())
        }
    }

    fun onCharacterClicked(character: Character) {
        _selectedCharacter.value = character
    }

    fun onNavigationCompleted() {
        _selectedCharacter.value = null
    }
}
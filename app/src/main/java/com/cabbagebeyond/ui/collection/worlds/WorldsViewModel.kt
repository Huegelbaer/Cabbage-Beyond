package com.cabbagebeyond.ui.collection.worlds

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cabbagebeyond.data.dao.WorldDao
import com.cabbagebeyond.data.repository.WorldRepository
import com.cabbagebeyond.model.World
import kotlinx.coroutines.launch

class WorldsViewModel: ViewModel() {

    private val _items = MutableLiveData<List<World>>()
    val items: LiveData<List<World>>
        get() = _items


    private val _selectedWorld = MutableLiveData<World>()
    val selectedWorld: LiveData<World>
        get() = _selectedWorld


    private lateinit var repository: WorldRepository

    init {
        repository = WorldRepository(WorldDao())

        viewModelScope.launch {
            val result = repository.getWorlds()
            _items.value = result.getOrNull() ?: listOf()
        }
    }
}
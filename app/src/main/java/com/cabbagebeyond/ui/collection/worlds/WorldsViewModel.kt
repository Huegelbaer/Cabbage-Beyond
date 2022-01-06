package com.cabbagebeyond.ui.collection.worlds

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cabbagebeyond.data.dto.WorldDTO
import com.cabbagebeyond.data.local.WorldDao
import com.cabbagebeyond.data.local.WorldRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.ArrayList

class WorldsViewModel: ViewModel() {

    private val _items = MutableLiveData<List<WorldDTO>>()
    val items: LiveData<List<WorldDTO>>
        get() = _items


    private val _selectedWorld = MutableLiveData<WorldDTO>()
    val selectedWorld: LiveData<WorldDTO>
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
package com.cabbagebeyond.ui.collection.worlds

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cabbagebeyond.data.WorldDataSource
import com.cabbagebeyond.model.User
import com.cabbagebeyond.model.World
import com.cabbagebeyond.ui.collection.CollectionListViewModel
import kotlinx.coroutines.launch

class WorldsViewModel(user: User, private val app: Application, private val dataSource: WorldDataSource) : CollectionListViewModel(user, app) {

    private val _items = MutableLiveData<List<World>>()
    val items: LiveData<List<World>>
        get() = _items

    private val _selectedWorld = MutableLiveData<World?>()
    val selectedWorld: LiveData<World?>
        get() = _selectedWorld

    lateinit var world: World
        private set

    init {
        viewModelScope.launch {
            val result = dataSource.getWorlds()
            val worlds = result.getOrDefault(listOf())
            _items.value = worlds
            if (worlds.isEmpty()) {
                showNoContentAvailable()
            }
        }
    }

    fun onSelectWorld(world: World) {
        this.world = world
        _selectedWorld.value = world
    }

    fun onNavigationCompleted() {
        _selectedWorld.value = null
    }
}
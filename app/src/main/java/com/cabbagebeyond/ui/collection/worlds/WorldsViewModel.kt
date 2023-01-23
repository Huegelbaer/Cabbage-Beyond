package com.cabbagebeyond.ui.collection.worlds

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.cabbagebeyond.data.WorldDataSource
import com.cabbagebeyond.model.User
import com.cabbagebeyond.model.World
import com.cabbagebeyond.ui.collection.CollectionListViewModel
import kotlinx.coroutines.launch
import java.util.UUID

class WorldsViewModel(user: User, private val app: Application, private val dataSource: WorldDataSource) : CollectionListViewModel(user, app) {

    private val _items = MutableLiveData<List<World>>()
    val items: LiveData<List<World>>
        get() = _items

    private val _selectedWorld = MutableLiveData<Pair<World, Boolean>?>()
    val selectedWorld: LiveData<Pair<World, Boolean>?>
        get() = _selectedWorld

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
        _selectedWorld.value = Pair(world, false)
    }

    fun addWorld() {
        _selectedWorld.value = Pair(World(UUID.randomUUID().toString()), true)
    }

    fun onNavigationCompleted() {
        _selectedWorld.value = null
    }
}
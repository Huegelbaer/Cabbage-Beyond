package com.cabbagebeyond.ui.collection.worlds

import android.app.Application
import androidx.lifecycle.viewModelScope
import com.cabbagebeyond.data.WorldDataSource
import com.cabbagebeyond.model.User
import com.cabbagebeyond.model.World
import com.cabbagebeyond.ui.collection.CollectionListViewModel
import kotlinx.coroutines.launch
import java.util.*

class WorldListViewModel(user: User, app: Application, private val dataSource: WorldDataSource) :
    CollectionListViewModel<World>(user, app) {


    init {
        viewModelScope.launch {
            val result = dataSource.getWorlds()
            val worlds = result.getOrDefault(listOf())
            mutableItems.value = worlds
            if (worlds.isEmpty()) {
                showNoContentAvailable()
            }
        }
    }

    fun addWorld() {
        val newWorld = World(UUID.randomUUID().toString())
        onCreateNewItem(newWorld)
    }
}
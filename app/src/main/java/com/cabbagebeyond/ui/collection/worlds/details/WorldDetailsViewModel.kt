package com.cabbagebeyond.ui.collection.worlds.details

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.cabbagebeyond.R
import com.cabbagebeyond.data.WorldDataSource
import com.cabbagebeyond.model.User
import com.cabbagebeyond.model.World
import com.cabbagebeyond.ui.DetailsViewModel
import com.cabbagebeyond.util.CollectionProperty
import kotlinx.coroutines.launch

class WorldDetailsViewModel(
    givenWorld: World,
    isEditingActive: Boolean,
    private val worldDataSource: WorldDataSource,
    user: User,
    app: Application
) : DetailsViewModel(user, isEditingActive, app) {

    var world = MutableLiveData(givenWorld)

    init {
        properties = arrayOf(
            CollectionProperty("name", R.string.character_name, ""),
            CollectionProperty("description", R.string.character_description, "")
        )
    }

    override fun onSave() {
        super.onSave()

        val toSafe = world.value ?: return
        viewModelScope.launch {
            if (save(toSafe)) {
                world.value = toSafe
                onSaveSucceeded()
            } else {
                onSaveFailed()
            }
        }
    }

    private suspend fun save(toSafe: World): Boolean {
        if (!validate(toSafe)) {
            message.value = R.string.save_failed
            return false
        }
        return worldDataSource.saveWorld(toSafe).isSuccess
    }

    private fun validate(world: World): Boolean {
        return world.name.isNotBlank()
    }

    override fun onPropertiesReceived(properties: Array<CollectionProperty>) {
        for (property in properties) {
            when (property.key) {
                "name" -> world.value?.name = property.value
                "description" -> world.value?.description += property.value
            }
        }
    }
}

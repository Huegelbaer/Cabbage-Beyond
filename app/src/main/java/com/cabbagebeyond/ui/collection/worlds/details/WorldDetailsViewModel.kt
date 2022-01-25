package com.cabbagebeyond.ui.collection.worlds.details

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.cabbagebeyond.R
import com.cabbagebeyond.data.WorldDataSource
import com.cabbagebeyond.model.User
import com.cabbagebeyond.model.World
import com.cabbagebeyond.ui.DetailsViewModel
import kotlinx.coroutines.launch

class WorldDetailsViewModel(givenWorld: World, private val worldDataSource: WorldDataSource, user: User, app: Application): DetailsViewModel(user, app) {

    var world = MutableLiveData(givenWorld)

    override fun onSave() {
        super.onSave()
        world.value?.let {
            save(it)
        }
    }

    private fun save(toSafe: World) {
        viewModelScope.launch {
            val result = worldDataSource.saveWorld(toSafe)
            if (result.isSuccess) {
                message.value = R.string.save_completed
                world.value = toSafe
            } else {
                message.value = R.string.save_failed
            }
        }
    }
}

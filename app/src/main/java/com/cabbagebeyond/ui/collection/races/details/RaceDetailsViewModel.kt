package com.cabbagebeyond.ui.collection.races.details

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.cabbagebeyond.R
import com.cabbagebeyond.data.RaceDataSource
import com.cabbagebeyond.data.WorldDataSource
import com.cabbagebeyond.model.Race
import com.cabbagebeyond.model.User
import com.cabbagebeyond.model.World
import com.cabbagebeyond.ui.DetailsViewModel
import kotlinx.coroutines.launch

class RaceDetailsViewModel(
    givenRace: Race,
    private val _raceDataSource: RaceDataSource,
    private val _worldDataSource: WorldDataSource,
    user: User,
    app: Application
) : DetailsViewModel(user, app) {

    var race = MutableLiveData(givenRace)

    private var _worlds = MutableLiveData<List<World?>>()
    val worlds: LiveData<List<World?>>
        get() = _worlds

    init {
    }

    override fun onEdit() {
        super.onEdit()
        if (_worlds.value == null) {
            loadWorlds()
        }
    }

    private fun loadWorlds() {
        viewModelScope.launch {
            val worlds: MutableList<World?> = _worldDataSource.getWorlds().getOrDefault(listOf()).toMutableList()
            worlds.add(0, null)
            _worlds.value = worlds
        }
    }

    private fun loadRanks() {

    }

    override fun onSave() {
        super.onSave()
        race.value?.let {
            save(it)
        }
    }

    private fun save(toSafe: Race) {
        viewModelScope.launch {
            val result = _raceDataSource.saveRace(toSafe)
            if (result.isSuccess) {
                message.value = R.string.save_completed
                race.value = toSafe
            } else {
                message.value = R.string.save_failed
            }
        }
    }

    fun onWorldSelected(world: World?) {
        val given = race.value
        given?.world = world
        race.value = given
    }
}
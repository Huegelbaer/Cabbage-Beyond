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
import com.cabbagebeyond.util.CollectionProperty
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class RaceDetailsViewModel(
    givenRace: Race,
    isEditingActive: Boolean,
    private val _raceDataSource: RaceDataSource,
    private val _worldDataSource: WorldDataSource,
    user: User,
    app: Application
) : DetailsViewModel(user, isEditingActive, app) {

    var race = MutableLiveData(givenRace)

    private var _worlds = MutableLiveData<List<World?>>()
    val worlds: LiveData<List<World?>>
        get() = _worlds

    private var _features = MutableLiveData<List<Race.Feature>>()
    val features: LiveData<List<Race.Feature>>
        get() = _features


    init {
        properties = arrayOf(
            CollectionProperty("name", R.string.character_name, ""),
            CollectionProperty("description", R.string.character_description, "")
        )

        _features.value = givenRace.raceFeatures

        loadRanks()
        loadWorlds()
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
            withContext(Dispatchers.Main) {
                _worlds.value = worlds
            }
        }
    }

    private fun loadRanks() {
        //TODO: Load ranks
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

    override fun onPropertiesReceived(properties: Array<CollectionProperty>) {
        for (property in properties) {
            when (property.key) {
                "name" -> race.value?.name = property.value
                "description" -> race.value?.description += property.value
            }
        }
    }
}
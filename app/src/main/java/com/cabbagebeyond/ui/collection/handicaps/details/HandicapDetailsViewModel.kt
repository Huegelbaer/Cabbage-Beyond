package com.cabbagebeyond.ui.collection.handicaps.details

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.cabbagebeyond.R
import com.cabbagebeyond.data.HandicapDataSource
import com.cabbagebeyond.data.WorldDataSource
import com.cabbagebeyond.model.Handicap
import com.cabbagebeyond.model.User
import com.cabbagebeyond.model.World
import com.cabbagebeyond.ui.DetailsViewModel
import com.cabbagebeyond.util.CollectionProperty
import kotlinx.coroutines.launch

class HandicapDetailsViewModel(
    private val _givenHandicap: Handicap,
    private val _handicapDataSource: HandicapDataSource,
    private val _worldDataSource: WorldDataSource,
    user: User,
    app: Application
) : DetailsViewModel(user, app) {

    var handicap = MutableLiveData(_givenHandicap)

    private var _worlds = MutableLiveData<List<World?>>()
    val worlds: LiveData<List<World?>>
        get() = _worlds

    private var _types = MutableLiveData<List<String>>()
    val types: LiveData<List<String>>
        get() = _types

    init {
        // for MVP the types are stored in resources.
        val stringArray = app.applicationContext.resources.getStringArray(R.array.types_of_handicap)
        _types.value = stringArray.toList()

        properties = arrayOf(
            CollectionProperty("name", R.string.character_name, ""),
            CollectionProperty("type", R.string.character_type, ""),
            CollectionProperty("description", R.string.character_description, "")
        )
    }

    override fun onEdit() {
        super.onEdit()
        if (_worlds.value == null) {
            loadWorlds()
        }
        if (_types.value == null) {
            loadTypes()
        }
    }

    private fun loadWorlds() {
        viewModelScope.launch {
            val worlds: MutableList<World?> = _worldDataSource.getWorlds().getOrDefault(listOf()).toMutableList()
            worlds.add(0, null)
            _worlds.value = worlds
        }
    }

    private fun loadTypes() {

    }

    override fun onSave() {
        super.onSave()
        handicap.value?.let {
            save(it)
        }
    }

    private fun save(toSafe: Handicap) {
        viewModelScope.launch {
            val result = _handicapDataSource.saveHandicap(toSafe)
            if (result.isSuccess) {
                message.value = R.string.save_completed
                handicap.value = toSafe
            } else {
                message.value = R.string.save_failed
            }
        }
    }

    fun onTypeSelected(rank: String) {
        handicap.value?.type = rank
    }

    fun onWorldSelected(world: World?) {
        val given = handicap.value
        given?.world = world
        handicap.value = given
    }

    override fun onPropertiesReceived(properties: Array<CollectionProperty>) {
        for (property in properties) {
            when (property.key) {
                "name" -> handicap.value?.name = property.value
                "type" -> handicap.value?.type = property.value
                "description" -> handicap.value?.description += property.value
            }
        }
    }
}
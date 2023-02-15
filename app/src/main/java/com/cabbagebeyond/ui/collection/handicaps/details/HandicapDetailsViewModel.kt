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
import com.cabbagebeyond.ui.collection.handicaps.HandicapType
import com.cabbagebeyond.util.CollectionProperty
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HandicapDetailsViewModel(
    givenHandicap: Handicap,
    isEditingActive: Boolean,
    private val _handicapDataSource: HandicapDataSource,
    private val _worldDataSource: WorldDataSource,
    user: User,
    app: Application
) : DetailsViewModel(user, isEditingActive, app) {

    var handicap = MutableLiveData(givenHandicap)

    private var _worlds = MutableLiveData<NullableSelection<World>>()
    val worlds: LiveData<NullableSelection<World>>
        get() = _worlds

    private var _types = MutableLiveData<Selection<HandicapType>>()
    val types: LiveData<Selection<HandicapType>>
        get() = _types

    init {

        properties = arrayOf(
            CollectionProperty("name", R.string.character_name, ""),
       //     CollectionProperty("type", R.string.character_type, ""),
            CollectionProperty("description", R.string.character_description, "")
        )

        loadTypes()
        loadWorlds()
    }

    override fun onEdit() {
        super.onEdit()

        _types.value?.values?.let { updateTypeSelection(it) }
        _worlds.value?.values?.let { updateWorldSelection(it) }
    }

    private fun loadTypes() {
        val application = getApplication<Application>()
        val types = Handicap.Type.values().map { HandicapType.create(it, application) }
        updateTypeSelection(types)
    }

    private fun updateTypeSelection(types: List<HandicapType>) {
        val application = getApplication<Application>()
        val currentSelected = handicap.value?.type?.let { HandicapType.create(it, application) }
        _types.value = Selection(currentSelected, types)
    }

    private fun loadWorlds() {
        viewModelScope.launch {
            val worlds: MutableList<World?> = _worldDataSource.getWorlds().getOrDefault(listOf()).toMutableList()
            worlds.add(0, null)
            withContext(Dispatchers.Main) {
                updateWorldSelection(worlds)
            }
        }
    }

    private fun updateWorldSelection(worlds: List<World?>) {
        _worlds.value = NullableSelection(handicap.value?.world, worlds)
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

    fun onTypeSelected(type: HandicapType) {
        handicap.value?.type = type.type
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
           //     "type" -> handicap.value?.type = property.value
                "description" -> handicap.value?.description += property.value
            }
        }
    }
}
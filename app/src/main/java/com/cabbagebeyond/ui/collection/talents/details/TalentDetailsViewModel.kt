package com.cabbagebeyond.ui.collection.talents.details

import android.app.Application
import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.cabbagebeyond.R
import com.cabbagebeyond.data.TalentDataSource
import com.cabbagebeyond.data.WorldDataSource
import com.cabbagebeyond.model.Talent
import com.cabbagebeyond.model.User
import com.cabbagebeyond.model.World
import com.cabbagebeyond.ui.DetailsViewModel
import com.cabbagebeyond.util.CollectionProperty
import kotlinx.coroutines.launch

class TalentDetailsViewModel(
    givenTalent: Talent,
    private val _talentDataSource: TalentDataSource,
    private val _worldDataSource: WorldDataSource,
    user: User,
    app: Application
) : DetailsViewModel(user, app) {

    var talent = MutableLiveData(givenTalent)

    private var _worlds = MutableLiveData<List<World?>>()
    val worlds: LiveData<List<World?>>
        get() = _worlds

    private var _types = MutableLiveData<List<String>>()
    val types: LiveData<List<String>>
        get() = _types

    private var _ranks = MutableLiveData<List<String>>()
    val ranks: LiveData<List<String>>
        get() = _ranks

    init {
        loadRanks(app.applicationContext)
        loadTypes(app.applicationContext)

        properties = arrayOf(
            CollectionProperty("name", R.string.character_name, ""),
            CollectionProperty("type", R.string.character_type, ""),
            CollectionProperty("rank", R.string.talent_rang_requirement, ""),
            CollectionProperty("description", R.string.character_description, "")
        )
    }

    override fun onEdit() {
        super.onEdit()
        if (_worlds.value == null) {
            loadWorlds()
        }
        /*
        if (_ranks.value == null) {
            loadRanks()
        }
        if (_types.value == null) {
            loadTypes()
        }
         */
    }

    private fun loadWorlds() {
        viewModelScope.launch {
            val worlds: MutableList<World?> = _worldDataSource.getWorlds().getOrDefault(listOf()).toMutableList()
            worlds.add(0, null)
            _worlds.value = worlds
        }
    }

    private fun loadRanks(context: Context) {
        // for MVP the ranks are stored in resources.
        val stringArray = context.resources.getStringArray(R.array.ranks)
        _ranks.value = stringArray.toList()

    }

    private fun loadTypes(context: Context) {
        // for MVP the types are stored in resources.
        val stringArray = context.resources.getStringArray(R.array.types_of_talents)
        _types.value = stringArray.toList()

    }

    override fun onSave() {
        super.onSave()
        talent.value?.let {
            save(it)
        }
    }

    private fun save(toSafe: Talent) {
        viewModelScope.launch {
            val result = _talentDataSource.saveTalent(toSafe)
            if (result.isSuccess) {
                message.value = R.string.save_completed
                talent.value = toSafe
            } else {
                message.value = R.string.save_failed
            }
        }
    }

    fun onRankSelected(rank: String) {
        talent.value?.rangRequirement = rank
    }

    fun onTypeSelected(rank: String) {
        talent.value?.type = rank
    }

    fun onWorldSelected(world: World?) {
        talent.value?.world = world
    }

    override fun onPropertiesReceived(properties: Array<CollectionProperty>) {
        for (property in properties) {
            when (property.key) {
                "name" -> talent.value?.name = property.value
                "attribute" -> talent.value?.rangRequirement = property.value
                "type" -> talent.value?.type = property.value
                "description" -> talent.value?.description += property.value
            }
        }
    }
}
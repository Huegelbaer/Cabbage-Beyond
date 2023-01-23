package com.cabbagebeyond.ui.collection.talents.details

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.cabbagebeyond.R
import com.cabbagebeyond.data.TalentDataSource
import com.cabbagebeyond.data.WorldDataSource
import com.cabbagebeyond.model.*
import com.cabbagebeyond.ui.DetailsViewModel
import com.cabbagebeyond.ui.collection.talents.TalentRank
import com.cabbagebeyond.ui.collection.talents.TalentType
import com.cabbagebeyond.util.CollectionProperty
import kotlinx.coroutines.launch

class TalentDetailsViewModel(
    givenTalent: Talent,
    isEditingActive: Boolean,
    private val _talentDataSource: TalentDataSource,
    private val _worldDataSource: WorldDataSource,
    user: User,
    app: Application
) : DetailsViewModel(user, isEditingActive, app) {

    data class RankSelection(var selected: TalentRank?, var values: List<TalentRank>)
    data class TypeSelection(var selected: TalentType?, var values: List<TalentType>)
    data class WorldSelection(var selected: World?, var values: List<World?>)

    var talent = MutableLiveData(givenTalent)

    private var _worlds = MutableLiveData<WorldSelection>()
    val worlds: LiveData<WorldSelection>
        get() = _worlds

    private var _types = MutableLiveData<TypeSelection>()
    val types: LiveData<TypeSelection>
        get() = _types

    private var _ranks = MutableLiveData<RankSelection>()
    val ranks: LiveData<RankSelection>
        get() = _ranks

    init {
        properties = arrayOf(
            CollectionProperty("name", R.string.character_name, ""),
        //    CollectionProperty("type", R.string.character_type, ""),
        //    CollectionProperty("rank", R.string.talent_rang_requirement, ""),
            CollectionProperty("description", R.string.character_description, "")
        )
    }

    override fun onEdit() {
        super.onEdit()

        _ranks.value?.values?.let { updateRankSelection(it) } ?: loadRanks()
        _types.value?.values?.let { updateTypeSelection(it) } ?: loadTypes()
        _worlds.value?.values?.let { updateWorldSelection(it) } ?: loadWorlds()
    }

    private fun loadRanks() {
        val application = getApplication<Application>()
        val attributes = Rank.values().map { TalentRank.create(it, application) }
        updateRankSelection(attributes)
    }

    private fun updateRankSelection(ranks: List<TalentRank>) {
        val application = getApplication<Application>()
        val currentSelected = talent.value?.rangRequirement?.let { TalentRank.create(it, application) }
        _ranks.value = RankSelection(currentSelected, ranks)
    }

    private fun loadTypes() {
        val application = getApplication<Application>()
        val attributes = Talent.Type.values().map { TalentType.create(it, application) }
        updateTypeSelection(attributes)
    }

    private fun updateTypeSelection(types: List<TalentType>) {
        val application = getApplication<Application>()
        val currentSelected = talent.value?.type?.let { TalentType.create(it, application) }
        _types.value = TypeSelection(currentSelected, types)
    }

    private fun loadWorlds() {
        viewModelScope.launch {
            val worlds: MutableList<World?> = _worldDataSource.getWorlds().getOrDefault(listOf()).toMutableList()
            worlds.add(0, null)
            updateWorldSelection(worlds)
        }
    }

    private fun updateWorldSelection(worlds: List<World?>) {
        _worlds.value = WorldSelection(talent.value?.world, worlds)
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

    fun onRankSelected(rank: TalentRank) {
        talent.value?.rangRequirement = rank.rank
    }

    fun onTypeSelected(type: TalentType) {
        talent.value?.type = type.type
    }

    fun onWorldSelected(world: World?) {
        talent.value?.world = world
    }

    override fun onPropertiesReceived(properties: Array<CollectionProperty>) {
        for (property in properties) {
            when (property.key) {
                "name" -> talent.value?.name = property.value
            //    "attribute" -> talent.value?.rangRequirement = property.value
            //    "type" -> talent.value?.type = property.value
                "description" -> talent.value?.description += property.value
            }
        }
    }
}
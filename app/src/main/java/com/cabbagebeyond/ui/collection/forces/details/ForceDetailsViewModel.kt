package com.cabbagebeyond.ui.collection.forces.details

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.cabbagebeyond.R
import com.cabbagebeyond.data.ForceDataSource
import com.cabbagebeyond.data.WorldDataSource
import com.cabbagebeyond.model.Force
import com.cabbagebeyond.model.Rank
import com.cabbagebeyond.model.User
import com.cabbagebeyond.model.World
import com.cabbagebeyond.ui.DetailsViewModel
import com.cabbagebeyond.ui.collection.forces.ForceRank
import com.cabbagebeyond.util.CollectionProperty
import kotlinx.coroutines.launch

class ForceDetailsViewModel(
    givenForce: Force,
    isEditingActive: Boolean,
    private val _forceDataSource: ForceDataSource,
    private val _worldDataSource: WorldDataSource,
    user: User,
    app: Application
) : DetailsViewModel(user, isEditingActive, app) {

    data class RankSelection(var selected: ForceRank?, var values: List<ForceRank>)
    data class WorldSelection(var selected: World?, var values: List<World?>)

    var force = MutableLiveData(givenForce)

    private var _worlds = MutableLiveData<WorldSelection>()
    val worlds: LiveData<WorldSelection>
        get() = _worlds

    private var _ranks = MutableLiveData<RankSelection>()
    val ranks: LiveData<RankSelection>
        get() = _ranks

    init {
        properties = arrayOf(
            CollectionProperty("name", R.string.character_name, ""),
            CollectionProperty("requirement", R.string.requirement_title, ""),
            CollectionProperty("cost", R.string.cost, ""),
            CollectionProperty("range", R.string.range, ""),
            CollectionProperty("description", R.string.character_description, "")
        )
    }

    override fun onEdit() {
        super.onEdit()

        _ranks.value?.values?.let { updateRankSelection(it) } ?: loadRanks()
        _worlds.value?.values?.let { updateWorldSelection(it) } ?: loadWorlds()

    }

    private fun loadRanks() {
        val application = getApplication<Application>()
        val attributes = Rank.values().map { ForceRank.create(it, application) }
        updateRankSelection(attributes)
    }

    private fun updateRankSelection(ranks: List<ForceRank>) {
        val application = getApplication<Application>()
        val currentSelected = force.value?.rangRequirement?.let { ForceRank.create(it, application) }
        _ranks.value = RankSelection(currentSelected, ranks)
    }

    private fun loadWorlds() {
        viewModelScope.launch {
            val worlds: MutableList<World?> = _worldDataSource.getWorlds().getOrDefault(listOf()).toMutableList()
            worlds.add(0, null)
            updateWorldSelection(worlds)
        }
    }

    private fun updateWorldSelection(worlds: List<World?>) {
        _worlds.value = WorldSelection(force.value?.world, worlds)
    }

    override fun onSave() {
        super.onSave()
        force.value?.let {
            save(it)
        }
    }

    private fun save(toSafe: Force) {
        viewModelScope.launch {
            val result = _forceDataSource.saveForce(toSafe)
            if (result.isSuccess) {
                message.value = R.string.save_completed
                force.value = toSafe
            } else {
                message.value = R.string.save_failed
            }
        }
    }

    fun onRankSelected(rank: ForceRank) {
        force.value?.rangRequirement = rank.rank
    }

    fun onWorldSelected(world: World?) {
        val given = force.value
        given?.world = world
        force.value = given
    }

    override fun onPropertiesReceived(properties: Array<CollectionProperty>) {
        for (property in properties) {
            when (property.key) {
                "name" -> force.value?.name = property.value
          //      "requirement" -> force.value?.rangRequirement = property.value
                "cost" -> force.value?.cost = property.value
                "range" -> force.value?.range = property.value
                "description" -> force.value?.description += property.value
            }
        }
    }
}
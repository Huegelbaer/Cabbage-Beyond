package com.cabbagebeyond.ui.collection.forces.details

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.cabbagebeyond.R
import com.cabbagebeyond.data.ForceDataSource
import com.cabbagebeyond.data.WorldDataSource
import com.cabbagebeyond.model.Force
import com.cabbagebeyond.model.User
import com.cabbagebeyond.model.World
import com.cabbagebeyond.ui.DetailsViewModel
import kotlinx.coroutines.launch

class ForceDetailsViewModel(
    givenForce: Force,
    private val _forceDataSource: ForceDataSource,
    private val _worldDataSource: WorldDataSource,
    user: User,
    app: Application
) : DetailsViewModel(user, app) {

    var force = MutableLiveData(givenForce)

    private var _worlds = MutableLiveData<List<World?>>()
    val worlds: LiveData<List<World?>>
        get() = _worlds

    private var _ranks = MutableLiveData<List<String>>()
    val ranks: LiveData<List<String>>
        get() = _ranks

    init {
        // for MVP the types are stored in resources.
        val stringArray = app.applicationContext.resources.getStringArray(R.array.ranks)
        _ranks.value = stringArray.toList()
    }

    override fun onEdit() {
        super.onEdit()
        if (_worlds.value == null) {
            loadWorlds()
        }
        if (_ranks.value == null) {
            loadRanks()
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

    fun onRankSelected(rank: String) {
        force.value?.rangRequirement = rank
    }

    fun onWorldSelected(world: World?) {
        val given = force.value
        given?.world = world
        force.value = given
    }
}
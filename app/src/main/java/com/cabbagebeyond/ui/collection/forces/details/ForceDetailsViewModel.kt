package com.cabbagebeyond.ui.collection.forces.details

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cabbagebeyond.R
import com.cabbagebeyond.data.ForceDataSource
import com.cabbagebeyond.data.WorldDataSource
import com.cabbagebeyond.model.Force
import com.cabbagebeyond.model.User
import com.cabbagebeyond.model.World
import com.cabbagebeyond.util.Feature
import kotlinx.coroutines.launch

class ForceDetailsViewModel(
    givenForce: Force,
    private val _forceDataSource: ForceDataSource,
    private val _worldDataSource: WorldDataSource,
    user: User,
    context: Context
) : ViewModel() {

    val userCanEdit = user.features.contains(Feature.CONFIGURE_APP.name)

    private var _isEditing = MutableLiveData(false)
    val isEditing: LiveData<Boolean>
        get() = _isEditing

    var force = MutableLiveData(givenForce)

    private var _fabImage = MutableLiveData(R.drawable.ic_edit)
    val fabImage: LiveData<Int>
        get() = _fabImage

    private var _worlds = MutableLiveData<List<World?>>()
    val worlds: LiveData<List<World?>>
        get() = _worlds

    private var _ranks = MutableLiveData<List<String>>()
    val ranks: LiveData<List<String>>
        get() = _ranks

    private var _message = MutableLiveData<Int?>()
    val message: LiveData<Int?>
        get() = _message

    init {
        // for MVP the types are stored in resources.
        val stringArray = context.resources.getStringArray(R.array.ranks)
        _ranks.value = stringArray.toList()
    }

    fun onClickFab() {
        val inEditMode = _isEditing.value ?: false

        if (inEditMode) {
            onSave()
        } else {
            onEdit()
        }
        _isEditing.value = !inEditMode
    }

    private fun onEdit() {
        _fabImage.value = R.drawable.ic_save
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

    private fun onSave() {
        force.value?.let {
            save(it)
        }
        _fabImage.value = R.drawable.ic_edit
    }

    private fun save(toSafe: Force) {
        viewModelScope.launch {
            val result = _forceDataSource.saveForce(toSafe)
            if (result.isSuccess) {
                _message.value = R.string.save_completed
                force.value = toSafe
            } else {
                _message.value = R.string.save_failed
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
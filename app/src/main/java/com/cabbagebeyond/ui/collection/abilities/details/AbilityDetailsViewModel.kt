package com.cabbagebeyond.ui.collection.abilities.details

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cabbagebeyond.R
import com.cabbagebeyond.data.AbilityDataSource
import com.cabbagebeyond.model.Ability
import com.cabbagebeyond.model.User
import com.cabbagebeyond.util.Feature
import kotlinx.coroutines.launch

class AbilityDetailsViewModel(private val _ability: Ability, private val _abilityDataSource: AbilityDataSource, private val user: User) : ViewModel() {

    val userCanEdit = user.features.contains(Feature.CONFIGURE_APP.name)

    private var _isEditing = MutableLiveData(false)
    val isEditing: LiveData<Boolean>
        get() = _isEditing

    var ability = MutableLiveData(_ability)

    private var _fabImage = MutableLiveData<Int>(R.drawable.ic_edit)
    val fabImage: LiveData<Int>
        get() = _fabImage



    init {

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
        _fabImage.value = R.drawable.ic_add
    }

    private fun onSave() {
        ability.value?.let {
            save(it)
        }
        _fabImage.value = R.drawable.ic_edit
    }

    private fun save(ability: Ability) {
        viewModelScope.launch {
            _abilityDataSource.saveAbility(ability)
        }
    }
}
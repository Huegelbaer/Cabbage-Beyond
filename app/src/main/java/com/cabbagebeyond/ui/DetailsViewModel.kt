package com.cabbagebeyond.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.map
import com.cabbagebeyond.R
import com.cabbagebeyond.model.User
import com.cabbagebeyond.util.Feature
import com.cabbagebeyond.util.CollectionProperty

open class DetailsViewModel(user: User, isEditingActive: Boolean, app: Application) : AndroidViewModel(app) {

    val userCanEdit = user.features.contains(Feature.CONFIGURE_APP.name)

    private var _isEditing = MutableLiveData(isEditingActive)
    val isEditing: LiveData<Boolean>
        get() = _isEditing

    val fabImage: LiveData<Int> = _isEditing.map {
        if (it) {
            R.drawable.ic_save
        } else {
            R.drawable.ic_edit
        }
    }

    var message = MutableLiveData<Int?>()
        protected set

    var properties: Array<CollectionProperty> = arrayOf()
        protected set

    open fun onPropertiesReceived(properties: Array<CollectionProperty>) {}

    fun onClickFab() {
        val inEditMode = _isEditing.value ?: false

        if (inEditMode) {
            onSave()
        } else {
            onEdit()
        }
    }

    open fun onSave() {
    }

    fun onSaveSucceeded() {
        message.value = R.string.save_completed
        toggleEditMode()
    }

    fun onSaveFailed() {
        message.value = R.string.save_failed
    }

    open fun onEdit() {
        toggleEditMode()
    }

    private fun toggleEditMode() {
        val inEditMode = _isEditing.value ?: false
        _isEditing.value = !inEditMode
    }
}
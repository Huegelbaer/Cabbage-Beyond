package com.cabbagebeyond.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.cabbagebeyond.R
import com.cabbagebeyond.model.User
import com.cabbagebeyond.util.Feature

open class DetailsViewModel(user: User, app: Application) : AndroidViewModel(app) {

    val userCanEdit = user.features.contains(Feature.CONFIGURE_APP.name)

    private var _isEditing = MutableLiveData(false)
    val isEditing: LiveData<Boolean>
        get() = _isEditing

    private var _fabImage = MutableLiveData(R.drawable.ic_edit)
    val fabImage: LiveData<Int>
        get() = _fabImage

    var message = MutableLiveData<Int?>()
        protected set

    fun onClickFab() {
        val inEditMode = _isEditing.value ?: false

        if (inEditMode) {
            onSave()
        } else {
            onEdit()
        }
        _isEditing.value = !inEditMode
    }

    open fun onSave() {
        _fabImage.value = R.drawable.ic_edit
    }

    open fun onEdit() {
        _fabImage.value = R.drawable.ic_save
    }
}
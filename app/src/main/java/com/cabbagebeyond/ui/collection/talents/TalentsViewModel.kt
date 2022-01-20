package com.cabbagebeyond.ui.collection.talents

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cabbagebeyond.data.TalentDataSource
import com.cabbagebeyond.model.Talent
import kotlinx.coroutines.launch

class TalentsViewModel(
    private val talentDataSource: TalentDataSource
) : ViewModel() {

    private var _items = MutableLiveData<List<Talent>>()
    val items: LiveData<List<Talent>>
        get() = _items

    private var _selectedTalent = MutableLiveData<Talent?>()
    val selectedTalent: LiveData<Talent?>
        get() = _selectedTalent

    init {
        viewModelScope.launch {
            _items.value = talentDataSource.getTalents().getOrDefault(listOf())
        }
    }

    fun onTalentClicked(talent: Talent) {
        _selectedTalent.value = talent
    }

    fun onNavigationCompleted() {
        _selectedTalent.value = null
    }
}
package com.cabbagebeyond.ui.collection.talents

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.cabbagebeyond.data.TalentDataSource
import com.cabbagebeyond.model.Talent
import com.cabbagebeyond.model.World
import com.cabbagebeyond.ui.collection.CollectionListViewModel
import kotlinx.coroutines.launch

class TalentsViewModel(
    private val app: Application,
    private val talentDataSource: TalentDataSource
) : CollectionListViewModel(app) {

    object Filter {
        var selectedType: TalentType? = null
        var selectedRank: TalentRank? = null
        var selectedWorld: World? = null
    }

    sealed class Interaction {
        data class OpenFilter(val types: FilterData<TalentType>, val ranks: FilterData<TalentRank>, val worlds: FilterData<World>) : Interaction()
    }

    private var _talents = listOf<Talent>()
    private var _items = MutableLiveData<List<Talent>>()
    val items: LiveData<List<Talent>>
        get() = _items

    private var _selectedTalent = MutableLiveData<Talent?>()
    val selectedTalent: LiveData<Talent?>
        get() = _selectedTalent

    private var _interaction = MutableLiveData<Interaction?>()
    val interaction: LiveData<Interaction?>
        get() = _interaction

    private var _activeFilter = Filter

    init {
        viewModelScope.launch {
            _talents = talentDataSource.getTalents().getOrDefault(listOf())
            _items.value = _talents
        }
    }

    fun onTalentClicked(talent: Talent) {
        _selectedTalent.value = talent
    }

    fun onNavigationCompleted() {
        _selectedTalent.value = null
    }

    override fun onSelectFilter() {
        val types = _talents.mapNotNull { talent -> talent.type?.let { TalentType.create(it, app) } }.toSet().toList()
        val ranks = _talents.mapNotNull { talent -> talent.rangRequirement?.let { TalentRank.create(it, app) } }.toSet().toList()
        val worlds = _talents.mapNotNull { it.world }.toSet().toList()

        _interaction.value = Interaction.OpenFilter(
            FilterData(types, _activeFilter.selectedType, TalentType::title),
            FilterData(ranks, _activeFilter.selectedRank, TalentRank::title),
            FilterData(worlds, _activeFilter.selectedWorld, World::name)
        )
    }

    fun filter(type: TalentType?, rank: TalentRank?, world: World?) {
        _activeFilter.selectedType = type
        _activeFilter.selectedRank = rank
        _activeFilter.selectedWorld = world

        viewModelScope.launch {
            _items.value = _talents.filter { talent ->
                val iType = type?.let { type ->
                    talent.type == type.type
                } ?: true
                val iRank = rank?.let { rank ->
                    talent.rangRequirement == rank.rank
                } ?: true
                val iWorld = world?.let { world ->
                    talent.world == world
                } ?: true

                iType && iRank && iWorld
            }
        }
    }

    fun onInteractionCompleted() {
        _interaction.value = null
    }
}
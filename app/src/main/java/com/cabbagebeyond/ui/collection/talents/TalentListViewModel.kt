package com.cabbagebeyond.ui.collection.talents

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.cabbagebeyond.R
import com.cabbagebeyond.data.TalentDataSource
import com.cabbagebeyond.model.Talent
import com.cabbagebeyond.model.User
import com.cabbagebeyond.model.World
import com.cabbagebeyond.ui.collection.CollectionListViewModel
import kotlinx.coroutines.launch
import java.util.*

class TalentListViewModel(
    user: User,
    private val app: Application,
    private val talentDataSource: TalentDataSource
) : CollectionListViewModel<Talent>(user, app) {

    object Filter {
        var selectedType: TalentType? = null
        var selectedRank: TalentRank? = null
        var selectedWorld: World? = null
    }

    sealed class Interaction {
        data class OpenFilter(
            val types: FilterData<TalentType>,
            val ranks: FilterData<TalentRank>,
            val worlds: FilterData<World>
        ) : Interaction()
    }

    private var _allItems = listOf<Talent>()

    private var _interaction = MutableLiveData<Interaction?>()
    val interaction: LiveData<Interaction?>
        get() = _interaction

    private var _activeFilter = Filter

    init {
        viewModelScope.launch {
            _allItems = talentDataSource.getTalents().getOrDefault(listOf())
            mutableItems.value = _allItems
            if (_allItems.isEmpty()) {
                showNoContentAvailable()
            }
        }
    }

    fun addTalent() {
        val newTalent = Talent(UUID.randomUUID().toString())
        onCreateNewItem(newTalent)
    }

    override fun onSelectFilter() {
        val application = getApplication<Application>()
        val types =
            _allItems.map { talent -> talent.type.let { TalentType.create(it, app) } }.toSet()
                .toList()
        val ranks =
            _allItems.map { talent -> talent.rangRequirement.let { TalentRank.create(it, app) } }
                .toSet().toList()
        val worlds = _allItems.mapNotNull { it.world }.toSet().toList()

        _interaction.value = Interaction.OpenFilter(
            FilterData(
                application.resources.getString(R.string.character_type),
                types,
                _activeFilter.selectedType,
                TalentType::title
            ),
            FilterData(
                application.resources.getString(R.string.talent_rang_requirement),
                ranks,
                _activeFilter.selectedRank,
                TalentRank::title
            ),
            FilterData(
                application.resources.getString(R.string.character_world),
                worlds,
                _activeFilter.selectedWorld,
                World::name
            )
        )
    }

    fun filter(type: TalentType?, rank: TalentRank?, world: World?) {
        _activeFilter.selectedType = type
        _activeFilter.selectedRank = rank
        _activeFilter.selectedWorld = world

        viewModelScope.launch {
            val filteredItems = _allItems.filter { talent ->
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
            mutableItems.value = filteredItems
            if (filteredItems.isEmpty()) {
                val searchTerm = listOfNotNull(type?.title, rank?.title, world?.name)
                showNoFilterResult(searchTerm) {
                    resetFilter()
                }
            } else {
                resetEmptyState()
            }
        }
    }

    fun onInteractionCompleted() {
        _interaction.value = null
    }

    private fun resetFilter() {
        _activeFilter.selectedType = null
        _activeFilter.selectedRank = null
        _activeFilter.selectedWorld = null
        mutableItems.value = _allItems
    }
}
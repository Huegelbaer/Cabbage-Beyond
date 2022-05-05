package com.cabbagebeyond.ui.collection.abilities.details

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.cabbagebeyond.R
import com.cabbagebeyond.data.AbilityDataSource
import com.cabbagebeyond.data.WorldDataSource
import com.cabbagebeyond.model.Ability
import com.cabbagebeyond.model.Attribute
import com.cabbagebeyond.model.User
import com.cabbagebeyond.model.World
import com.cabbagebeyond.ui.DetailsViewModel
import com.cabbagebeyond.util.CollectionProperty
import kotlinx.coroutines.launch

class AbilityDetailsViewModel(
    givenAbility: Ability,
    private val _abilityDataSource: AbilityDataSource,
    private val _worldDataSource: WorldDataSource,
    user: User,
    app: Application
) : DetailsViewModel(user, app) {

    data class AbilityAttribute(var attribute: Attribute, var title: String)
    data class WorldSelection(var selected: World?, var values: List<World?>)
    data class AttributeSelection(var selected: AbilityAttribute?, var values: List<AbilityAttribute>)

    var ability = MutableLiveData(givenAbility)

    private var _worlds = MutableLiveData<WorldSelection>()
    val worlds: LiveData<WorldSelection>
        get() = _worlds

    private var _attributes = MutableLiveData<AttributeSelection>()
    val attributes: LiveData<AttributeSelection>
        get() = _attributes

    init {

        properties = arrayOf(
            CollectionProperty("name", R.string.character_name, ""),
         //   CollectionProperty("attribute", R.string.attribute, ""),
            CollectionProperty("description", R.string.character_description, "")
        )
    }

    override fun onEdit() {
        super.onEdit()
        _worlds.value?.values?.let { updateWorldSelection(it) } ?: loadWorlds()

        _attributes.value?.values?.let { updateAttributeSelection(it) } ?: loadAttributes()
    }

    private fun loadWorlds() {
        viewModelScope.launch {
            val worlds: MutableList<World?> = _worldDataSource.getWorlds().getOrDefault(listOf()).toMutableList()
            worlds.add(0, null)
            updateWorldSelection(worlds)
        }
    }

    private fun updateWorldSelection(worlds: List<World?>) {
        _worlds.value = WorldSelection(ability.value?.world, worlds)
    }

    private fun loadAttributes() {
        // for MVP the attributes are stored as enum.
        val attributes = Attribute.values().map { createAbilityAttribute(it) }
        updateAttributeSelection(attributes)
    }

    private fun updateAttributeSelection(attributes: List<AbilityAttribute>) {
        val currentSelected = ability.value?.attribute?.let { createAbilityAttribute(it) }
        _attributes.value = AttributeSelection(currentSelected, attributes)
    }

    override fun onSave() {
        super.onSave()
        ability.value?.let {
            save(it)
        }
    }

    private fun save(toSafe: Ability) {
        viewModelScope.launch {
            val result = _abilityDataSource.saveAbility(toSafe)
            if (result.isSuccess) {
                message.value = R.string.save_completed
                ability.value = toSafe
            } else {
                message.value = R.string.save_failed
            }
        }
    }

    fun onAttributeSelected(attribute: AbilityAttribute) {
        val givenAbility = ability.value
        givenAbility?.attribute = attribute.attribute
        ability.value = givenAbility
    }

    fun onWorldSelected(world: World?) {
        val givenAbility = ability.value
        givenAbility?.world = world
        ability.value = givenAbility
    }

    override fun onPropertiesReceived(properties: Array<CollectionProperty>) {
        for (property in properties) {
            when (property.key) {
                "name" -> ability.value?.name = property.value
               // "attribute" -> ability.value?.attribute = property.value
                "description" -> ability.value?.description += property.value
            }
        }
    }

    private fun createAbilityAttribute(attribute: Attribute): AbilityAttribute {
        val titleId = when (attribute) {
            Attribute.STRENGTH -> R.string.attribute_strength
            Attribute.INTELLECT -> R.string.attribute_intellect
            Attribute.CONSTITUTION -> R.string.attribute_constitution
            Attribute.DEXTERITY -> R.string.attribute_dexterity
            Attribute.WILLPOWER -> R.string.attribute_willpower
        }
        val title = getApplication<Application>().resources.getString(titleId)
        return AbilityAttribute(attribute, title)
    }
}
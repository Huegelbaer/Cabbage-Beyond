package com.cabbagebeyond.ui

import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.cabbagebeyond.model.Role
import android.view.View
import android.widget.ImageView
import com.cabbagebeyond.R
import com.cabbagebeyond.model.Attribute
import com.cabbagebeyond.model.Character


@BindingAdapter("roleList")
fun bindTextViewToRoleList(textView: TextView, roles: List<Role>) {
    textView.text = roles.joinToString("\n") { it.name }
}

@BindingAdapter("textAsList")
fun bindTextViewToListOfStrings(textView: TextView, roles: List<String>) {
    textView.text = roles.joinToString("\n")
}

@BindingAdapter("descriptionContent", "descriptionParameter")
fun bindContentDescription(view: View, content: String, parameter: String) {
    view.contentDescription = String.format(content, parameter)
}

@BindingAdapter("srcCompat")
fun bindSrcCompat(imageView: ImageView, resource: Int) {
    imageView.setImageResource(resource)
}

@BindingAdapter("visibility")
fun bindVisibility(view: View, isVisible: Boolean?) {
    view.visibility = when (isVisible) {
        true -> View.VISIBLE
        false -> View.INVISIBLE
        null -> View.GONE
    }
}

@BindingAdapter("characterType")
fun bindTextViewToCharacterType(textView: TextView, type: Character.Type) {
    val resourceId = getTitleIdForCharacterType(type)
    textView.text = textView.context.getString(resourceId)
}

private fun getTitleIdForCharacterType(type: Character.Type): Int  {
    return when (type) {
        Character.Type.PLAYER -> R.string.character_type_player
        Character.Type.NPC -> R.string.character_type_npc
        Character.Type.MONSTER -> R.string.character_type_monster
    }
}

@BindingAdapter("abilityAttribute")
fun bindTextViewToAbilityAttribute(textView: TextView, attribute: Attribute) {
    val resourceId = getTitleIdForAbilityAttribute(attribute)
    textView.text = textView.context.getString(resourceId)
}

fun getTitleIdForAbilityAttribute(attribute: Attribute): Int  {
    return when (attribute) {
        Attribute.STRENGTH -> R.string.attribute_strength
        Attribute.INTELLECT -> R.string.attribute_intellect
        Attribute.CONSTITUTION -> R.string.attribute_constitution
        Attribute.DEXTERITY -> R.string.attribute_dexterity
        Attribute.WILLPOWER -> R.string.attribute_willpower
    }
}
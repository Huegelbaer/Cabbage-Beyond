package com.cabbagebeyond.ui.collection.characters

import android.content.Context
import com.cabbagebeyond.R
import com.cabbagebeyond.model.Character


data class CharacterType(var type: Character.Type, var title: String) {

    companion object {
        fun create(type: Character.Type, context: Context): CharacterType {
            val titleId = when (type) {
                Character.Type.PLAYER -> R.string.character_type_player
                Character.Type.NPC -> R.string.character_type_npc
                Character.Type.MONSTER -> R.string.character_type_monster
            }
            val title = context.resources.getString(titleId)
            return CharacterType(type, title)
        }
    }
}

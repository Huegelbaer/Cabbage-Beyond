package com.cabbagebeyond.data.local

import com.cabbagebeyond.model.Rank

enum class CharacterRank {
    ROOKIE,
    ADVANCED,
    VETERAN,
    HERO,
    LEGEND
}

fun valueToRank(dtoValue: String?): CharacterRank {
    return when (dtoValue) {
        "Anfänger" -> CharacterRank.ROOKIE
        "Fortgeschritten" -> CharacterRank.ADVANCED
        "Veteran" -> CharacterRank.VETERAN
        "Held" -> CharacterRank.HERO
        "Legende" -> CharacterRank.LEGEND
        else -> CharacterRank.ROOKIE
    }
}

fun Rank.asDatabaseModel(): CharacterRank {
    return when (this) {
        Rank.ROOKIE -> CharacterRank.ROOKIE
        Rank.ADVANCED -> CharacterRank.ADVANCED
        Rank.VETERAN -> CharacterRank.VETERAN
        Rank.HERO -> CharacterRank.HERO
        Rank.LEGEND -> CharacterRank.LEGEND
    }
}

fun CharacterRank.asDomainModel(): Rank {
    return when (this) {
        CharacterRank.ROOKIE -> Rank.ROOKIE
        CharacterRank.ADVANCED -> Rank.ADVANCED
        CharacterRank.VETERAN -> Rank.VETERAN
        CharacterRank.HERO -> Rank.HERO
        CharacterRank.LEGEND -> Rank.LEGEND
    }
}
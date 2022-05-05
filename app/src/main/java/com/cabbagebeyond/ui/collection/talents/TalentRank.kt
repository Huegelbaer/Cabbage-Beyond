package com.cabbagebeyond.ui.collection.talents

import android.content.Context
import com.cabbagebeyond.R
import com.cabbagebeyond.model.Rank

data class TalentRank(var rank: Rank, var title: String) {

    companion object {
        fun create(rank: Rank, context: Context): TalentRank {
            val titleId = when (rank) {
                Rank.ROOKIE -> R.string.rank_rookie
                Rank.ADVANCED -> R.string.rank_advanced
                Rank.VETERAN -> R.string.rank_veteran
                Rank.HERO -> R.string.rank_hero
                Rank.LEGEND -> R.string.rank_legend
            }
            val title = context.resources.getString(titleId)
            return TalentRank(rank, title)
        }
    }
}

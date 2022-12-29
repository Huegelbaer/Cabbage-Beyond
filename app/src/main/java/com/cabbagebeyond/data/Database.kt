package com.cabbagebeyond.data

import com.cabbagebeyond.data.local.dao.*

object Database {

    val characterDao = CharacterDao()
    val forceDao = ForceDao()
    val handicapDao = HandicapDao()
    val raceDao = RaceDao()
    val roleDao = RoleDao()
    val sessionDao = SessionDao()
    val storyDao = StoryDao()
    val talentDao = TalentDao()
    val userDao = UserDao()

}
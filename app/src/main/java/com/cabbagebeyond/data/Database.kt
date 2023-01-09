package com.cabbagebeyond.data

import com.cabbagebeyond.data.local.dao.*

object Database {

    val roleDao = RoleDao()
    val sessionDao = SessionDao()
    val storyDao = StoryDao()
    val userDao = UserDao()

}
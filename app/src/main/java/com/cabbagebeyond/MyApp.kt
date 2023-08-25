package com.cabbagebeyond

import android.app.Application
import com.cabbagebeyond.data.*
import com.cabbagebeyond.data.local.CabbageDatabase
import com.cabbagebeyond.data.remote.service.*
import com.cabbagebeyond.data.repository.*
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.koin.dsl.module

class MyApp : Application() {

    val database: CabbageDatabase by lazy { CabbageDatabase.getDatabase(this) }

    override fun onCreate() {
        super.onCreate()

        /**
         * use Koin Library as a service locator
         */
        val myModule = module {
            single { AbilityRepository(database.abilityDao(), AbilityService()) }
            single { CharacterRepository(database.characterDao(), CharacterService()) }
            single { EquipmentRepository(database.equipmentDao(), EquipmentService()) }
            single { ForceRepository(database.forceDao(), ForceService()) }
            single { HandicapRepository(database.handicapDao(), HandicapService()) }
            single { RaceRepository(database.raceDao(), RaceService()) }
            single { RoleRepository(database.roleDao(), RoleService()) }
            single { SessionRepository(database.sessionDao(), SessionService()) }
            single { StoryRepository(database.storyDao(), StoryService()) }
            single { TalentRepository(database.talentDao(), TalentService()) }
            single { UserRepository(database.userDao(), UserService()) }
            single { WorldRepository(database.worldDao(), WorldService()) }
        }

        startKoin {
            androidContext(this@MyApp)
            modules(listOf(myModule))
        }
    }
}
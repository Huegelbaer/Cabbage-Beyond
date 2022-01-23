package com.cabbagebeyond

import android.app.Application
import com.cabbagebeyond.data.*
import com.cabbagebeyond.data.repository.*
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.koin.dsl.module

class MyApp: Application() {

    override fun onCreate() {
        super.onCreate()

        /**
         * use Koin Library as a service locator
         */
        val myModule = module {
            single { AbilityRepository(Database.abilityDao, get()) as AbilityDataSource }
            single { CharacterRepository(Database.characterDao, get(), get(), get(), get(), get(), get(), get()) as CharacterDataSource }
            single { EquipmentRepository(Database.equipmentDao, get()) as EquipmentDataSource }
            single { ForceRepository(Database.forceDao, get()) as ForceDataSource }
            single { HandicapRepository(Database.handicapDao, get()) as HandicapDataSource }
            single { RaceRepository(Database.raceDao) as RaceDataSource }
            single { RoleRepository(Database.roleDao) as RoleDataSource }
            single { SessionRepository(Database.sessionDao) as SessionDataSource }
            single { StoryRepository(Database.storyDao) as StoryDataSource }
            single { TalentRepository(Database.talentDao) as TalentDataSource }
            single { UserRepository(Database.userDao) as UserDataSource }
            single { WorldRepository(Database.worldDao) as WorldDataSource }
        }

        startKoin {
            androidContext(this@MyApp)
            modules(listOf(myModule))
        }
    }
}
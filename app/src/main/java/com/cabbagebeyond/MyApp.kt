package com.cabbagebeyond

import android.app.Application
import com.cabbagebeyond.data.*
import com.cabbagebeyond.data.local.CabbageDatabase
import com.cabbagebeyond.data.remote.*
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

            single {
                AbilityRepository(
                    database.abilityDao(),
                    AbilityService(),
                    get()
                ) as AbilityDataSource
            }
            single {
                CharacterRepository(
                    Database.characterDao,
                    CharacterService(),
                    get(),
                    get(),
                    get(),
                    get(),
                    get(),
                    get(),
                    get()
                ) as CharacterDataSource
            }
            single {
                EquipmentRepository(
                    database.equipmentDao(),
                    EquipmentService(),
                    get()
                ) as EquipmentDataSource
            }
            single { ForceRepository(database.forceDao(), ForceService(), get()) as ForceDataSource }
            single {
                HandicapRepository(
                    database.handicapDao(),
                    HandicapService(),
                    get()
                ) as HandicapDataSource
            }
            single { RaceRepository(database.raceDao(), RaceService(), get()) as RaceDataSource }
            single { RoleRepository(Database.roleDao, RoleService()) as RoleDataSource }
            single { SessionRepository(Database.sessionDao) as SessionDataSource }
            single { StoryRepository(Database.storyDao) as StoryDataSource }
            single {
                TalentRepository(
                    Database.talentDao,
                    TalentService(),
                    get()
                ) as TalentDataSource
            }
            single { UserRepository(Database.userDao, UserService()) as UserDataSource }
            single { WorldRepository(database.worldDao(), WorldService()) as WorldDataSource }
        }

        startKoin {
            androidContext(this@MyApp)
            modules(listOf(myModule))
        }
    }
}
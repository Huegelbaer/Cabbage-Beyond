package com.cabbagebeyond.util

import android.content.Context
import android.util.Log
import androidx.work.*

import com.cabbagebeyond.data.*
import org.koin.java.KoinJavaComponent.inject

class RefreshDataWorker(appContext: Context, params: WorkerParameters):
    CoroutineWorker(appContext, params) {

    companion object {
        private const val TAG = "RefreshDataWorker"
    }

    override suspend fun doWork(): Result {
        Log.d(TAG, "doWork")

        return try {
            refreshRoles()

            refreshWorlds()
            refreshAbilities()
            refreshEquipments()
            refreshForces()
            refreshHandicaps()
            refreshRaces()
            refreshTalents()
            //refreshUsers()
            refreshCharacters()

            Log.d(TAG, "success")
            Result.success()

        } catch (e: Throwable) {
            Log.d(TAG, "work failed ${e.localizedMessage}")
            Result.retry()
        }
    }

    private suspend fun refreshAbilities() {
        val abilityDataSource: AbilityDataSource by inject(AbilityDataSource::class.java)
        abilityDataSource.refreshAbilities()
    }

    private suspend fun refreshCharacters() {
        val characterDataSource: CharacterDataSource by inject(CharacterDataSource::class.java)
        characterDataSource.refreshCharacters()
    }

    private suspend fun refreshEquipments() {
        val equipmentDataSource: EquipmentDataSource by inject(EquipmentDataSource::class.java)
        equipmentDataSource.refreshEquipments()
    }

    private suspend fun refreshForces() {
        val forceDataSource: ForceDataSource by inject(ForceDataSource::class.java)
        forceDataSource.refreshForces()
    }

    private suspend fun refreshHandicaps() {
        val handicapDataSource: HandicapDataSource by inject(HandicapDataSource::class.java)
        handicapDataSource.refreshHandicaps()
    }

    private suspend fun refreshRaces() {
        val raceDataSource: RaceDataSource by inject(RaceDataSource::class.java)
        raceDataSource.refreshRaces()
    }

    private suspend fun refreshRoles() {
        val roleDataSource: RoleDataSource by inject(RoleDataSource::class.java)
        roleDataSource.refreshRoles()
    }

    private suspend fun refreshTalents() {
        val talentDataSource: TalentDataSource by inject(TalentDataSource::class.java)
        talentDataSource.refreshTalents()
    }

    private suspend fun refreshUsers() {
        val userDataSource: UserDataSource by inject(UserDataSource::class.java)
        userDataSource.refreshUsers()
    }

    private suspend fun refreshWorlds() {
        val worldDataSource: WorldDataSource by inject(WorldDataSource::class.java)
        worldDataSource.refreshWorlds()
    }

}

fun startRefreshWorker(context: Context) {
    val request = OneTimeWorkRequestBuilder<RefreshDataWorker>()
        .setExpedited(OutOfQuotaPolicy.RUN_AS_NON_EXPEDITED_WORK_REQUEST)
        .build()

    WorkManager.getInstance(context)
        .enqueue(request)
}
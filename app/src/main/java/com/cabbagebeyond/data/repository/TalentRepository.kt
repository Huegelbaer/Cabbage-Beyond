package com.cabbagebeyond.data.repository

import com.cabbagebeyond.data.TalentDataSource
import com.cabbagebeyond.data.dao.TalentDao
import com.cabbagebeyond.data.dto.asDatabaseModel
import com.cabbagebeyond.data.dto.asDomainModel
import com.cabbagebeyond.model.Talent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class TalentRepository(
    private val talentDao: TalentDao,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : TalentDataSource {

    override suspend fun getTalents(): Result<List<Talent>> = withContext(ioDispatcher) {
        return@withContext talentDao.getTalents().mapCatching { it.asDomainModel() }
    }

    override suspend fun getTalent(id: String): Result<Talent> = withContext(ioDispatcher) {
        return@withContext talentDao.getTalent(id).mapCatching { it.asDomainModel() }
    }

    override suspend fun saveTalent(talent: Talent) = withContext(ioDispatcher) {
        talentDao.saveTalent(talent.asDatabaseModel())
    }

    override suspend fun deleteTalent(id: String) = withContext(ioDispatcher) {
        talentDao.deleteTalent(id)
    }
}
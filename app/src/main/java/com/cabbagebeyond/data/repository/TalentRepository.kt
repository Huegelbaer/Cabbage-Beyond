package com.cabbagebeyond.data.repository

import com.cabbagebeyond.data.TalentDataSource
import com.cabbagebeyond.data.dto.TalentDTO
import com.cabbagebeyond.data.dao.TalentDao
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class TalentRepository(
    private val talentDao: TalentDao,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : TalentDataSource {

    override suspend fun getTalents(): Result<List<TalentDTO>> = withContext(ioDispatcher) {
        return@withContext talentDao.getTalents()
    }

    override suspend fun getTalent(id: String): Result<TalentDTO> = withContext(ioDispatcher) {
        return@withContext talentDao.getTalent(id)
    }

    override suspend fun saveTalent(talent: TalentDTO) = withContext(ioDispatcher) {
        talentDao.saveTalent(talent)
    }

    override suspend fun deleteTalent(id: String) = withContext(ioDispatcher) {
        talentDao.deleteTalent(id)
    }
}
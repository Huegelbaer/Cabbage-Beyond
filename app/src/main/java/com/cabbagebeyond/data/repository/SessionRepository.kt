package com.cabbagebeyond.data.repository

import com.cabbagebeyond.data.SessionDataSource
import com.cabbagebeyond.data.dto.SessionDTO
import com.cabbagebeyond.data.dao.SessionDao
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class SessionRepository(
    private val sessionDao: SessionDao,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : SessionDataSource {

    override suspend fun getSessions(): Result<List<SessionDTO>> = withContext(ioDispatcher) {
        return@withContext sessionDao.getSessions()
    }

    override suspend fun getSession(id: String): Result<SessionDTO> = withContext(ioDispatcher) {
        return@withContext sessionDao.getSession(id)
    }

    override suspend fun saveSession(session: SessionDTO) = withContext(ioDispatcher) {
        sessionDao.saveSession(session)
    }

    override suspend fun deleteSession(id: String) = withContext(ioDispatcher) {
        sessionDao.deleteSession(id)
    }
}
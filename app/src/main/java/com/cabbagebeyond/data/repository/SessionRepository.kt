package com.cabbagebeyond.data.repository

import com.cabbagebeyond.data.SessionDataSource
import com.cabbagebeyond.data.dao.SessionDao
import com.cabbagebeyond.data.dto.asDatabaseModel
import com.cabbagebeyond.data.dto.asDomainModel
import com.cabbagebeyond.model.Session
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class SessionRepository(
    private val sessionDao: SessionDao,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : SessionDataSource {

    override suspend fun getSessions(): Result<List<Session>> = withContext(ioDispatcher) {
        return@withContext sessionDao.getSessions().mapCatching { it.asDomainModel() }
    }

    override suspend fun getSession(id: String): Result<Session> = withContext(ioDispatcher) {
        return@withContext sessionDao.getSession(id).mapCatching { it.asDomainModel() }
    }

    override suspend fun saveSession(session: Session) = withContext(ioDispatcher) {
        sessionDao.saveSession(session.asDatabaseModel())
    }

    override suspend fun deleteSession(id: String) = withContext(ioDispatcher) {
        sessionDao.deleteSession(id)
    }
}
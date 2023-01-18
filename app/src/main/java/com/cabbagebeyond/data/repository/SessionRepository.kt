package com.cabbagebeyond.data.repository

import com.cabbagebeyond.data.SessionDataSource
import com.cabbagebeyond.data.dto.SessionDTO
import com.cabbagebeyond.data.local.dao.SessionDao
import com.cabbagebeyond.data.local.entities.SessionEntity
import com.cabbagebeyond.data.local.relations.SessionWithEverything
import com.cabbagebeyond.data.remote.SessionService
import com.cabbagebeyond.model.Session
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class SessionRepository(
    private val sessionDao: SessionDao,
    private val sessionService: SessionService,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : SessionDataSource {

    override suspend fun getSessions(): Result<List<Session>> = withContext(ioDispatcher) {
        val result = sessionDao.getSessions()
        val list = result.map { it.asDomainModel() }
        return@withContext Result.success(list)
    }

    override suspend fun getSession(id: String): Result<Session> = withContext(ioDispatcher) {
        val result = sessionDao.getSession(id)
        val list = result.asDomainModel()
        return@withContext Result.success(list)
    }

    override suspend fun saveSession(session: Session) = withContext(ioDispatcher) {
        sessionDao.saveSession(session.asDatabaseModel())
    }

    override suspend fun deleteSession(session: Session) = withContext(ioDispatcher) {
        sessionDao.deleteSession(session.asDatabaseModel())
    }
}

private fun List<SessionDTO>.asDatabaseModel(): List<SessionEntity> {
    return map {
        it.asDatabaseModel()
    }
}

private fun SessionDTO.asDatabaseModel(): SessionEntity {
    return SessionEntity(name, description, player, status, invitedPlayers, owner, story, id)
}

private fun Session.asDatabaseModel(): SessionEntity {
    return SessionEntity(name, description, player, status, invitedPlayers.map { it.id }, owner.id, story.id, id)
}

private fun SessionWithEverything.asDomainModel(): Session {
    return Session(session.name, session.description, session.player, session.status, players.map { it.asDomainModel(
        listOf()
    ) }, owner.asDomainModel(listOf()), story.asDomainModel(owner.asDomainModel(listOf()), null), session.id)
}
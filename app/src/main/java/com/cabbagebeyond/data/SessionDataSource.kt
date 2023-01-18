package com.cabbagebeyond.data

import com.cabbagebeyond.model.Session

interface SessionDataSource {

    //fun observeSessions(): LiveData<Result<List<Session>>>

    suspend fun getSessions(): Result<List<Session>>

    //suspend fun refreshSessions()

    //fun observeSession(id: String): LiveData<Result<Session>>

    suspend fun getSession(id: String): Result<Session>

    //suspend fun refreshSession(id: String)

    suspend fun saveSession(session: Session)

    //suspend fun deleteAllSessions()

    suspend fun deleteSession(session: Session)
}
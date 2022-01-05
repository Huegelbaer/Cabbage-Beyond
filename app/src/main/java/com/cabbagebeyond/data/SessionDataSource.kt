package com.cabbagebeyond.data

import com.cabbagebeyond.data.dto.SessionDTO

interface SessionDataSource {

    //fun observeSessions(): LiveData<Result<List<SessionDTO>>>

    suspend fun getSessions(): Result<List<SessionDTO>>

    //suspend fun refreshSessions()

    //fun observeSession(id: String): LiveData<Result<SessionDTO>>

    suspend fun getSession(id: String): Result<SessionDTO>

    //suspend fun refreshSession(id: String)

    suspend fun saveSession(session: SessionDTO)

    //suspend fun deleteAllSessions()

    suspend fun deleteSession(id: String)
}
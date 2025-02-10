package com.ftorres.notes.domain.repository

import com.ftorres.notes.domain.model.User

interface AuthRepository {
    suspend fun login(username: String, password: String): Boolean
    suspend fun register(user: User): Boolean
    suspend fun loginWithOAuth(token: String): Boolean
    fun getCurrentUser(): User?

}

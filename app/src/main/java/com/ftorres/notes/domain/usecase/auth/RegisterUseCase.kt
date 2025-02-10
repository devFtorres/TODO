package com.ftorres.notes.domain.usecase.auth

import com.ftorres.notes.domain.model.User
import com.ftorres.notes.domain.repository.AuthRepository
import javax.inject.Inject

class RegisterUseCase @Inject constructor(
    private val repository: AuthRepository
) {
    suspend operator fun invoke(user: User): Boolean {
        return repository.register(user)
    }
}

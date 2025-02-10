package com.ftorres.notes.data.repository

import android.util.Log
import com.ftorres.notes.data.local.dao.UserDao
import com.ftorres.notes.data.local.entity.UserEntity
import com.ftorres.notes.domain.model.User
import com.ftorres.notes.domain.repository.AuthRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepositoryImpl @Inject constructor(
    private val userDao: UserDao,
    private val firebaseAuth: FirebaseAuth
) : AuthRepository {

    override suspend fun login(email: String, password: String): Boolean {
        return try {
            val authResult = firebaseAuth.signInWithEmailAndPassword(email, password).await()
            authResult.user?.let { firebaseUser ->
                val localUser = userDao.getUserByEmail(email)
                if (localUser == null) {
                    userDao.insertUser(
                        UserEntity(id = firebaseUser.uid, email = email, username = firebaseUser.displayName ?: email, password = password)
                    )
                }
                true
            } ?: false
        } catch (e: Exception) {
            Log.e("AuthRepository", "Erro no login", e)
            false
        }
    }

    override suspend fun register(user: User): Boolean {
        return try {
            val authResult = firebaseAuth.createUserWithEmailAndPassword(user.email, user.password).await()
            authResult.user?.let { firebaseUser ->
                userDao.insertUser(UserEntity(id = firebaseUser.uid, email = user.email, username = user.username, password = user.password))
                true
            } ?: false
        } catch (e: Exception) {
            Log.e("AuthRepository", "Erro ao registrar usuário", e)
            false
        }
    }

    override suspend fun loginWithOAuth(token: String): Boolean {
        return try {
            val credential = GoogleAuthProvider.getCredential(token, null)
            val authResult = firebaseAuth.signInWithCredential(credential).await()
            authResult.user?.let { firebaseUser ->
                val user = User(id = firebaseUser.uid, email = firebaseUser.email ?: "", username = firebaseUser.displayName ?: "Usuário desconhecido", password = "")
                if (userDao.getUserByEmail(user.email) == null) {
                    userDao.insertUser(UserEntity(id = user.id, email = user.email, username = user.username, password = ""))
                }
                true
            } ?: false
        } catch (e: Exception) {
            Log.e("AuthRepository", "Erro no login OAuth", e)
            false
        }
    }

    override fun getCurrentUser(): User? {
        return firebaseAuth.currentUser?.let {
            User(id = it.uid, email = it.email ?: "", username = it.displayName ?: it.email ?: "Usuário desconhecido", password = "")
        }
    }
}

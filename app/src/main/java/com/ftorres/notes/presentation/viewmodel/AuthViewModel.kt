package com.ftorres.notes.presentation.viewmodel

import android.app.Application
import android.content.Intent
import android.content.IntentSender
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ftorres.notes.R
import com.ftorres.notes.domain.model.User
import com.ftorres.notes.domain.repository.AuthRepository
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.android.gms.common.api.ApiException
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val application: Application
) : ViewModel() {

    private val _googleSignInResult = MutableLiveData<Result<String>>()
    val googleSignInResult: LiveData<Result<String>> get() = _googleSignInResult

    private val signInClient: SignInClient = Identity.getSignInClient(application)

    fun register(email: String, username: String, password: String, onSuccess: () -> Unit, onError: (String) -> Unit) {
        viewModelScope.launch {
            try {
                val user = User(id = "", email = email, username = username, password = password)
                val result = authRepository.register(user)
                if (result) {
                    onSuccess()
                } else {
                    onError("Erro ao registrar usuário!")
                }
            } catch (e: Exception) {
                onError("Erro ao registrar: ${e.message}")
            }
        }
    }

    fun handleSignInResult(intent: Intent?) {
        try {
            val credential = signInClient.getSignInCredentialFromIntent(intent)
            val googleToken = credential.googleIdToken
            if (googleToken != null) {
                loginWithOAuth(
                    token = googleToken,
                    onSuccess = { _googleSignInResult.value = Result.success("Login bem-sucedido!") },
                    onError = { error -> _googleSignInResult.value = Result.failure(Exception(error)) }
                )
            } else {
                _googleSignInResult.value = Result.failure(Exception("Token do Google nulo"))
            }
        } catch (e: ApiException) {
            _googleSignInResult.value = Result.failure(e)
        }
    }

    fun login(username: String, password: String, onSuccess: () -> Unit, onError: (String) -> Unit) {
        viewModelScope.launch {
            val result = authRepository.login(username, password)
            if (result) onSuccess() else onError("Credenciais inválidas")
        }
    }

    fun loginWithOAuth(token: String, onSuccess: () -> Unit, onError: (String) -> Unit) {
        viewModelScope.launch {
            val result = authRepository.loginWithOAuth(token)
            if (result) onSuccess() else onError("Falha ao autenticar com OAuth")
        }
    }

    fun isUserLoggedIn(): Boolean {
        return authRepository.getCurrentUser() != null
    }

    suspend fun getSignInIntent(): IntentSender? {
        return try {
            val signInRequest = BeginSignInRequest.builder()
                .setGoogleIdTokenRequestOptions(
                    BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                        .setSupported(true)
                        .setServerClientId(application.getString(R.string.default_web_client_id))
                        .setFilterByAuthorizedAccounts(false)
                        .build()
                )
                .build()

            val result = signInClient.beginSignIn(signInRequest).await()
            result.pendingIntent.intentSender
        } catch (e: Exception) {
            _googleSignInResult.value = Result.failure(Exception("Erro ao obter Intent de Sign-In: ${e.message}"))
            null
        }
    }
}

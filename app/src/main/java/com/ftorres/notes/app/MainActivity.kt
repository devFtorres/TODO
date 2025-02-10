package com.ftorres.notes.app

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.work.*
import androidx.navigation.compose.rememberNavController
import com.ftorres.notes.presentation.viewmodel.AuthViewModel
import com.ftorres.notes.presentation.viewmodel.NotesViewModel
import dagger.hilt.android.AndroidEntryPoint
import com.ftorres.notes.core.navigation.NavGraph
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.auth.api.identity.SignInClient
import com.ftorres.notes.data.sync.NotesSyncWorker
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val authViewModel: AuthViewModel by viewModels()
    private val notesViewModel: NotesViewModel by viewModels()

    private lateinit var signInClient: SignInClient

    private val googleSignInLauncher = registerForActivityResult(
        ActivityResultContracts.StartIntentSenderForResult()
    ) { result ->
        authViewModel.handleSignInResult(result.data)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        signInClient = Identity.getSignInClient(this)

        setContent {
            val navController = rememberNavController()
            NavGraph(
                navController = navController,
                authViewModel = authViewModel,
                notesViewModel = notesViewModel,
                onGoogleSignIn = { signInWithGoogle() }
            )
        }

        authViewModel.googleSignInResult.observe(this, Observer { result ->
            result.onSuccess { message ->
                println("Login bem-sucedido: $message")
            }.onFailure { error ->
                println("Erro ao entrar com Google: ${error.message}")
            }
        })

        // Configurar o WorkManager para sincronizar as notas periodicamente
        scheduleSyncWorker()
    }

    private fun signInWithGoogle() {
        lifecycleScope.launch {
            val intentSender = authViewModel.getSignInIntent()
            if (intentSender != null) {
                val intentSenderRequest = IntentSenderRequest.Builder(intentSender).build()
                googleSignInLauncher.launch(intentSenderRequest)
            } else {
                println("Erro ao iniciar login com Google.")
            }
        }
    }

    private fun scheduleSyncWorker() {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED) // Apenas quando online
            .build()

        val syncRequest = PeriodicWorkRequestBuilder<NotesSyncWorker>(15, TimeUnit.MINUTES)
            .setConstraints(constraints)
            .build()

        WorkManager.getInstance(this).enqueueUniquePeriodicWork(
            "NotesSyncWorker",
            ExistingPeriodicWorkPolicy.KEEP,
            syncRequest
        )
    }
}
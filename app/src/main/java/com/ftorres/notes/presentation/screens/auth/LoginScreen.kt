package com.ftorres.notes.presentation.screens.auth

import android.app.Activity
import android.content.Intent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.ftorres.notes.presentation.viewmodel.AuthViewModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import kotlinx.coroutines.launch

@Composable
fun LoginScreen(
    viewModel: AuthViewModel,
    onLoginSuccess: () -> Unit,
    onNavigateToRegister: () -> Unit,
    onGoogleSignIn: () -> Unit // 🔥 Adicionando o parâmetro correto!
) {
    val context = LocalContext.current as Activity
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    // Configuração correta do Google Sign-In
    val googleSignInClient = remember {
        GoogleSignIn.getClient(
            context,
            GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(context.getString(com.ftorres.notes.R.string.default_web_client_id))
                .requestEmail()
                .build()
        )
    }

    // Launcher para processar o resultado do login com Google
    val googleSignInLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        val data: Intent? = result.data
        if (result.resultCode == Activity.RESULT_OK && data != null) {
            val account = GoogleSignIn.getSignedInAccountFromIntent(data)
            account.addOnSuccessListener { googleAccount ->
                val googleToken = googleAccount.idToken
                if (googleToken != null) {
                    viewModel.loginWithOAuth(
                        token = googleToken,
                        onSuccess = { onLoginSuccess() },
                        onError = { errorMessage = it }
                    )
                } else {
                    errorMessage = "Erro ao autenticar com Google"
                }
            }.addOnFailureListener {
                errorMessage = "Falha ao autenticar: ${it.message}"
            }
        } else {
            errorMessage = "Falha ao autenticar"
        }
    }

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Login", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = username,
            onValueChange = { username = it },
            label = { Text("Usuário") }
        )
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Senha") }
        )
        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = {
            viewModel.login(username, password, onSuccess = {
                onLoginSuccess()
            }, onError = { errorMessage = it })
        }) {
            Text("Entrar")
        }

        errorMessage?.let {
            Text(text = it, color = MaterialTheme.colorScheme.error)
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = { onGoogleSignIn() }) { // 🔥 Agora chamamos a função corretamente
            Text("Entrar com Google")
        }

        Spacer(modifier = Modifier.height(16.dp))

        TextButton(onClick = onNavigateToRegister) {
            Text("Não tem uma conta? Registre-se")
        }
    }
}

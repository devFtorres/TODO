package com.ftorres.notes.presentation.screens.auth

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.ftorres.notes.presentation.viewmodel.AuthViewModel

@Composable
fun RegisterScreen(
    viewModel: AuthViewModel,
    onRegisterSuccess: () -> Unit,
    onNavigateToLogin: () -> Unit
) {
    var email by remember { mutableStateOf("") }
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Registrar", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(16.dp))

        // Campo de e-mail (Novo)
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("E-mail") },
            singleLine = true
        )
        Spacer(modifier = Modifier.height(8.dp))

        // Campo de nome de usuário
        OutlinedTextField(
            value = username,
            onValueChange = { username = it },
            label = { Text("Usuário") },
            singleLine = true
        )
        Spacer(modifier = Modifier.height(8.dp))

        // Campo de senha
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Senha") },
            visualTransformation = PasswordVisualTransformation(),
            singleLine = true
        )
        Spacer(modifier = Modifier.height(8.dp))

        // Campo de confirmação de senha
        OutlinedTextField(
            value = confirmPassword,
            onValueChange = { confirmPassword = it },
            label = { Text("Confirmar Senha") },
            visualTransformation = PasswordVisualTransformation(),
            singleLine = true
        )
        Spacer(modifier = Modifier.height(16.dp))

        // Botão de Registro
        Button(
            onClick = {
                when {
                    email.isEmpty() || username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty() -> {
                        errorMessage = "Todos os campos são obrigatórios"
                    }
                    "@" !in email || "." !in email -> {
                        errorMessage = "Por favor, insira um e-mail válido"
                    }
                    password.length < 6 -> {
                        errorMessage = "A senha deve ter pelo menos 6 caracteres"
                    }
                    password != confirmPassword -> {
                        errorMessage = "As senhas não coincidem"
                    }
                    else -> {
                        viewModel.register(email, username, password,
                            onSuccess = { onRegisterSuccess() },
                            onError = { errorMessage = it }
                        )
                    }
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Registrar")
        }

        // Mensagem de erro
        errorMessage?.let {
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = it, color = MaterialTheme.colorScheme.error)
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Botão para alternar para login
        TextButton(onClick = onNavigateToLogin) {
            Text("Já tem uma conta? Entre aqui")
        }
    }
}

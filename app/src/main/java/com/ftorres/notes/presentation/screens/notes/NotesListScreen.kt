package com.ftorres.notes.presentation.screens.notes

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.work.Data
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.ftorres.notes.core.navigation.Destinations
import com.ftorres.notes.data.sync.NotificationWorker
import com.ftorres.notes.presentation.components.NoteCard
import com.ftorres.notes.presentation.viewmodel.NotesViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotesListScreen(viewModel: NotesViewModel, navController: NavController, onAddNote: () -> Unit) {
    val notes by viewModel.notes.collectAsState()
    val context = LocalContext.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Minhas Notas") },
                actions = {
                    IconButton(onClick = { viewModel.forceSync() }) {
                        Icon(Icons.Default.Refresh, contentDescription = "Sincronizar")
                    }
                    IconButton(onClick = { triggerNotification(context) }) {
                        Icon(Icons.Default.Notifications, contentDescription = "Testar Notificação")
                    }
                }
            )
        },
        floatingActionButton = {
            Column {
                FloatingActionButton(onClick = onAddNote) {
                    Text("+")
                }
                Spacer(modifier = Modifier.height(16.dp))
                FloatingActionButton(onClick = { navController.navigate(Destinations.Map.route) }) {
                    Text("🗺")
                }
            }
        }
    ) { padding ->
        Column(modifier = Modifier.padding(padding)) {
            if (notes.isEmpty()) {
                Text("Nenhuma nota encontrada", modifier = Modifier.padding(16.dp))
            } else {
                LazyColumn {
                    items(notes) { note ->
                        NoteCard(
                            note = note,
                            onClick = {},
                            onDelete = { viewModel.deleteNote(note) }
                        )
                    }
                }
            }
        }
    }
}


/**
 * Função para testar a notificação via WorkManager
 */

fun triggerNotification(context: android.content.Context) {
    val data = Data.Builder()
        .putString("noteTitle", "Exemplo de Notificação")
        .build()

    val notificationRequest = OneTimeWorkRequestBuilder<NotificationWorker>()
        .setInputData(data)
        .build()

    WorkManager.getInstance(context).enqueue(notificationRequest)
}

package com.ftorres.notes.presentation.screens.notes

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.ftorres.notes.presentation.components.NoteCard
import com.ftorres.notes.presentation.viewmodel.NotesViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotesListScreen(viewModel: NotesViewModel, onAddNote: () -> Unit) {
    val notes by viewModel.notes.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Minhas Notas") },
                actions = {
                    Button(onClick = { viewModel.forceSync() }) {
                        Text("Sincronizar")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onAddNote) {
                Text("+")
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

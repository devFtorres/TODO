package com.ftorres.notes.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ftorres.notes.domain.model.Note
import com.ftorres.notes.domain.usecase.notes.CreateNoteUseCase
import com.ftorres.notes.domain.usecase.notes.DeleteNoteUseCase
import com.ftorres.notes.domain.usecase.notes.GetNotesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NotesViewModel @Inject constructor(
    private val getNotesUseCase: GetNotesUseCase,
    private val createNoteUseCase: CreateNoteUseCase,
    private val deleteNoteUseCase: DeleteNoteUseCase
) : ViewModel() {

    private val _notes = MutableStateFlow<List<Note>>(emptyList())
    val notes: StateFlow<List<Note>> get() = _notes

    init {
        syncNotes()
    }

    private fun syncNotes() {
        viewModelScope.launch {
            getNotesUseCase().collect { notes ->
                _notes.value = notes // ✅ Atualiza a UI automaticamente
            }
        }
    }

    fun addNote(title: String, content: String) {
        viewModelScope.launch {
            createNoteUseCase(Note(title = title, content = content, timestamp = System.currentTimeMillis()))
            syncNotes()
        }
    }

    fun deleteNote(note: Note) {
        viewModelScope.launch {
            deleteNoteUseCase(note) // ✅ Remove do banco de dados
            _notes.value = _notes.value.filter { it.id != note.id }
        }
    }

    fun forceSync() {
        syncNotes() // 🔥 Permite sincronizar manualmente os dados com a API
    }
}

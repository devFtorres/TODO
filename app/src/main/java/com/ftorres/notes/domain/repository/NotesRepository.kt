package com.ftorres.notes.domain.repository

import com.ftorres.notes.domain.model.Note
import kotlinx.coroutines.flow.Flow

interface NotesRepository {
    fun getAllNotes(): Flow<List<Note>>
    suspend fun insertNote(note: Note)
    suspend fun deleteNote(note: Note)
    suspend fun updateNoteDueDate(noteId: Int, dueDate: Long)
}

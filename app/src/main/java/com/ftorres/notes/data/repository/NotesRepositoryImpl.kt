package com.ftorres.notes.data.repository

import com.ftorres.notes.data.local.dao.NoteDao
import com.ftorres.notes.data.local.entity.NoteEntity
import com.ftorres.notes.data.remote.NotesApiService
import com.ftorres.notes.data.remote.dto.toDto
import com.ftorres.notes.data.remote.dto.toDomain
import com.ftorres.notes.domain.model.Note
import com.ftorres.notes.domain.repository.NotesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NotesRepositoryImpl @Inject constructor(
    private val noteDao: NoteDao,
    private val api: NotesApiService
) : NotesRepository {

    override fun getAllNotes(): Flow<List<Note>> {
        return noteDao.getAllNotes().map { notes ->
            notes.map { it.toDomain() }
        }
    }

    override suspend fun insertNote(note: Note) {
        noteDao.insertNote(note.toEntity()) // ✅ Salva localmente
        try {
            api.createNote(note.toDto()) // ✅ Envia para API
        } catch (e: Exception) {
            println("⚠️ Sem internet, salvando localmente para sincronizar depois.")
        }
    }

    override suspend fun deleteNote(note: Note) {
        noteDao.deleteNoteById(note.id) // ✅ Remove do banco local
        try {
            api.deleteNote(note.id) // ✅ Remove da API
        } catch (e: Exception) {
            println("⚠️ Sem internet, irá remover da API depois.")
        }
    }

    private fun NoteEntity.toDomain(): Note {
        return Note(id, title, content, timestamp)
    }

    private fun Note.toEntity(): NoteEntity {
        return NoteEntity(id, title, content, timestamp)
    }
}

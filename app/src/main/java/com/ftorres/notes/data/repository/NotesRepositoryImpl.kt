package com.ftorres.notes.data.repository

import com.ftorres.notes.data.local.dao.NoteDao
import com.ftorres.notes.data.local.entity.NoteEntity
import com.ftorres.notes.data.remote.NotesApiService
import com.ftorres.notes.data.remote.dto.NoteDto
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
        return noteDao.getAllNotes().map { notes -> notes.map { it.toDomain() } }
    }

    override suspend fun insertNote(note: Note) {
        noteDao.insertNote(note.toEntity())
        try {
            api.createNote(note.toDto())
        } catch (e: Exception) {
            println("Sem internet, a salvar localmente para sincronizar depois.")
        }
    }

    override suspend fun deleteNote(note: Note) {
        noteDao.deleteNoteById(note.id)
        try {
            api.deleteNote(note.id)
        } catch (e: Exception) {
            println("Sem internet, irá remover da API depois.")
        }
    }

    override suspend fun updateNoteDueDate(noteId: Int, dueDate: Long) {
        noteDao.updateNoteDueDate(noteId, dueDate)
        try {
            api.updateNoteDueDate(noteId, dueDate)
        } catch (e: Exception) {
            println("Sem internet, atualização será sincronizada depois.")
        }
    }

    private fun Note.toEntity() = NoteEntity(
        id = id,
        title = title,
        content = content,
        timestamp = timestamp,
        dueDate = dueDate,
        imageUrl = imageUrl,
        latitude = latitude,
        longitude = longitude
    )

    private fun NoteEntity.toDomain() = Note(
        id = id,
        title = title,
        content = content,
        timestamp = timestamp,
        dueDate = dueDate,
        imageUrl = imageUrl,
        latitude = latitude,
        longitude = longitude
    )

    private fun Note.toDto() = NoteDto(
        id = id,
        title = title,
        content = content,
        timestamp = timestamp,
        dueDate = dueDate,
        imageUrl = imageUrl,
        latitude = latitude,
        longitude = longitude
    )
}
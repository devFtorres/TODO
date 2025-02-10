package com.ftorres.notes.domain.usecase.notes


import com.ftorres.notes.domain.model.Note
import com.ftorres.notes.domain.repository.NotesRepository
import kotlinx.coroutines.flow.Flow

class GetNotesUseCase(private val repository: NotesRepository) {
    operator fun invoke(): Flow<List<Note>> {
        return repository.getAllNotes()
    }
}

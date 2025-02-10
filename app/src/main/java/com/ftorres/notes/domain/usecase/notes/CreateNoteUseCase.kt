package com.ftorres.notes.domain.usecase.notes


import com.ftorres.notes.domain.model.Note
import com.ftorres.notes.domain.repository.NotesRepository

class CreateNoteUseCase(private val repository: NotesRepository) {
    suspend operator fun invoke(note: Note) {
        repository.insertNote(note)
    }
}

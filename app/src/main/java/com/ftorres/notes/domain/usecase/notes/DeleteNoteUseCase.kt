package com.ftorres.notes.domain.usecase.notes

import com.ftorres.notes.domain.model.Note
import com.ftorres.notes.domain.repository.NotesRepository
import javax.inject.Inject

class DeleteNoteUseCase @Inject constructor(
    private val notesRepository: NotesRepository
) {
    suspend operator fun invoke(note: Note) {
        notesRepository.deleteNote(note)
    }
}

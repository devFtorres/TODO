package com.ftorres.notes.data.remote.dto

import com.ftorres.notes.domain.model.Note

data class NoteDto(
    val id: Int,
    val title: String,
    val content: String,
    val timestamp: Long
)

fun NoteDto.toDomain(): Note {
    return Note(id, title, content, timestamp)
}

fun Note.toDto(): NoteDto {
    return NoteDto(id, title, content, timestamp)
}

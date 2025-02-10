package com.ftorres.notes.data.remote.dto

import com.ftorres.notes.data.local.entity.NoteEntity
import com.ftorres.notes.domain.model.Note

data class NoteDto(
    val id: Int,
    val title: String,
    val content: String,
    val timestamp: Long,
    val dueDate: Long? = null,
    val imageUrl: String? = null,
    val latitude: Double? = null,
    val longitude: Double? = null
)

fun NoteDto.toDomain(): Note {
    return Note(id, title, content, timestamp, null, imageUrl, latitude, longitude)
}

fun Note.toDto(): NoteDto {
    return NoteDto(
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

fun NoteEntity.toDomain(): Note {
    return Note(id, title, content, timestamp, dueDate, imageUrl, latitude, longitude)
}
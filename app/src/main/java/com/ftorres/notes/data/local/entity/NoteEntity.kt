package com.ftorres.notes.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.ftorres.notes.data.remote.dto.NoteDto

@Entity(tableName = "notes")
data class NoteEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val content: String,
    val timestamp: Long
)
fun NoteEntity.toDto(): NoteDto {
    return NoteDto(id = this.id, title = this.title, content = this.content, timestamp = this.timestamp)
}
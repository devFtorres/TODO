package com.ftorres.notes.domain.model


data class Note(
    val id: Int,
    val title: String,
    val content: String,
    val timestamp: Long,
    val dueDate: Long?,
    val imageUrl: String? = null,
    val latitude: Double? = null,
    val longitude: Double? = null
)
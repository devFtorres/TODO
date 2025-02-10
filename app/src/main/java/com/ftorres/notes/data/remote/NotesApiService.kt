package com.ftorres.notes.data.remote

import com.ftorres.notes.data.remote.dto.NoteDto
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.DELETE
import retrofit2.http.Path

interface NotesApiService {
    @GET("notes")
    suspend fun getNotes(): List<NoteDto>

    @POST("notes")
    suspend fun createNote(@Body note: NoteDto)

    @DELETE("notes/{id}")
    suspend fun deleteNote(@Path("id") id: Int)
}

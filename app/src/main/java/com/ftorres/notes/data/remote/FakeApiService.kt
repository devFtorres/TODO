package com.ftorres.notes.data.remote

import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Body

data class NoteApiModel(
    val id: String,
    val title: String,
    val content: String
)

interface FakeApiService {
    @GET("notes")
    suspend fun getNotes(): List<NoteApiModel>

    @POST("notes")
    suspend fun createNote(@Body note: NoteApiModel)
}

package com.ftorres.notes.domain.model

data class User(
    val id: String = "",
    val email: String,
    val username: String,
    val password: String
)

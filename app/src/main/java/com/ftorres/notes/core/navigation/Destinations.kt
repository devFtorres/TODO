package com.ftorres.notes.core.navigation


sealed class Destinations(val route: String) {
    object Login : Destinations("login")
    object Register : Destinations("register")
    object NotesList : Destinations("notes_list")
    object NoteDetail : Destinations("note_detail")
}

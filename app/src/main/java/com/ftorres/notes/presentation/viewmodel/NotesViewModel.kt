package com.ftorres.notes.presentation.viewmodel

import android.app.Application
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.ftorres.notes.data.sync.NotificationWorker
import com.ftorres.notes.domain.model.Note
import com.ftorres.notes.domain.usecase.notes.CreateNoteUseCase
import com.ftorres.notes.domain.usecase.notes.DeleteNoteUseCase
import com.ftorres.notes.domain.usecase.notes.GetNotesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.io.File
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltViewModel
class NotesViewModel @Inject constructor(
    private val getNotesUseCase: GetNotesUseCase,
    private val createNoteUseCase: CreateNoteUseCase,
    private val deleteNoteUseCase: DeleteNoteUseCase,
    private val application: Application
) : ViewModel() {

    private val _notes = MutableStateFlow<List<Note>>(emptyList())
    val notes: StateFlow<List<Note>> get() = _notes

    init {
        syncNotes()
    }

    private fun syncNotes() {
        viewModelScope.launch {
            getNotesUseCase().collect { notes ->
                _notes.value = notes
            }
        }
    }

    private suspend fun uploadImage(uri: Uri): String? {
        return try {
            val file = File(application.cacheDir, "temp_image_${System.currentTimeMillis()}")
            application.contentResolver.openInputStream(uri)?.use { input ->
                file.outputStream().use { output ->
                    input.copyTo(output)
                }
            }
            file.absolutePath
        } catch (e: Exception) {
            null
        }
    }

    fun addNoteWithImage(
        title: String,
        content: String,
        imageUri: Uri?,
        dueDate: Long? = null,
        latitude: Double? = null,
        longitude: Double? = null
    ) {
        viewModelScope.launch {
            val imageUrl = imageUri?.let { uploadImage(it) }
            val note = Note(
                id = 0,
                title = title,
                content = content,
                timestamp = System.currentTimeMillis(),
                dueDate = dueDate,
                imageUrl = imageUrl,
                latitude = latitude,
                longitude = longitude
            )
            createNoteUseCase(note)
            note.dueDate?.let { scheduleNotification(note) }
            syncNotes()
        }
    }

    fun deleteNote(note: Note) {
        viewModelScope.launch {
            deleteNoteUseCase(note)
            _notes.value = _notes.value.filter { it.id != note.id }
            WorkManager.getInstance(application)
                .cancelUniqueWork("note_notification_${note.id}")
        }
    }

    private fun scheduleNotification(note: Note) {
        note.dueDate?.let { dueDate ->
            val notificationWork = OneTimeWorkRequestBuilder<NotificationWorker>()
                .setInitialDelay(dueDate - System.currentTimeMillis(), TimeUnit.MILLISECONDS)
                .setInputData(workDataOf(
                    "noteId" to note.id,
                    "noteTitle" to note.title
                ))
                .build()

            WorkManager.getInstance(application)
                .enqueueUniqueWork(
                    "note_notification_${note.id}",
                    ExistingWorkPolicy.REPLACE,
                    notificationWork
                )
        }
    }

    fun forceSync() {
        syncNotes()
    }
}
package com.ftorres.notes.data.sync

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.ftorres.notes.data.local.dao.NoteDao
import com.ftorres.notes.data.remote.NotesApiService
import com.ftorres.notes.data.remote.dto.toDomain
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import com.ftorres.notes.data.remote.dto.toDto

class NotesSyncWorker(
    context: Context,
    workerParams: WorkerParameters,
    private val noteDao: NoteDao,
    private val api: NotesApiService
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result {
        return withContext(Dispatchers.IO) {
            try {
                val localNotes = noteDao.getAllNotes().first().map { it.toDomain() }
                val remoteNotes = api.getNotes().map { it.toDomain() }


                for (note in localNotes) {
                    if (!remoteNotes.any { it.id == note.id }) {
                        api.createNote(note.toDto())
                    }
                }

                Result.success()
            } catch (e: Exception) {
                println("⚠️ Erro ao sincronizar: ${e.message}")
                Result.retry()
            }
        }
    }
}

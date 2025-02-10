package com.ftorres.notes.data.sync

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.ftorres.notes.data.local.dao.NoteDao
import com.ftorres.notes.data.local.entity.toDto
import com.ftorres.notes.data.remote.NotesApiService
import com.ftorres.notes.data.remote.dto.toDomain
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext

class NotesSyncWorker(
    context: Context,
    workerParams: WorkerParameters,
    private val noteDao: NoteDao,
    private val api: NotesApiService
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result {
        return withContext(Dispatchers.IO) {
            try {
                val localNotes = noteDao.getAllNotes().first() // 🔥 Obtém notas locais
                val remoteNotes = api.getNotes().map { it.toDomain() } // 🔥 Obtém notas remotas e converte

                // Sincronizar notas locais para a API
                for (note in localNotes) {
                    if (!remoteNotes.any { it.id == note.id }) {
                        api.createNote(note.toDto()) // 🔥 Envia para API
                    }
                }

                Result.success()
            } catch (e: Exception) {
                println("⚠️ Erro ao sincronizar: ${e.message}")
                Result.retry() // 🔄 Tenta novamente depois
            }
        }
    }
}

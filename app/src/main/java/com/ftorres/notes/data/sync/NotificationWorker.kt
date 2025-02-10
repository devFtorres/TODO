package com.ftorres.notes.data.sync

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.ftorres.notes.R

class NotificationWorker(
    context: Context,
    params: WorkerParameters
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        val noteId = inputData.getInt("noteId", 0)
        val noteTitle = inputData.getString("noteTitle") ?: "Nota sem título"

        Log.d("NotificationWorker", "Iniciando worker para nota ID: $noteId, título: $noteTitle")

        createNotification(noteTitle)
        return Result.success()
    }

    private fun createNotification(title: String) {
        val notificationManager = applicationContext.getSystemService(
            Context.NOTIFICATION_SERVICE
        ) as NotificationManager?

        if (notificationManager == null) {
            Log.e("NotificationWorker", "NotificationManager é nulo, notificação não será exibida.")
            return
        }

        val channelId = "notes_channel"
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Lembretes de Notas",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Canal de notificações para lembretes de notas"
            }
            notificationManager.createNotificationChannel(channel)
        }

        val notification = NotificationCompat.Builder(applicationContext, channelId)
            .setContentTitle("Lembrete de Nota")
            .setContentText("Nota: $title")
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .build()

        with(NotificationManagerCompat.from(applicationContext)) {
            if (ActivityCompat.checkSelfPermission(
                    applicationContext,
                    android.Manifest.permission.POST_NOTIFICATIONS
                ) == android.content.pm.PackageManager.PERMISSION_GRANTED
            ) {
                notify(System.currentTimeMillis().toInt(), notification)
                Log.d("NotificationWorker", "Notificação enviada com sucesso!")
            } else {
                Log.e("NotificationWorker", "Permissão para notificações não concedida!")
            }
        }
    }
}

package com.example.doralfleetcontrol

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat

class ReminderReceiver : BroadcastReceiver() {

    companion object {
        const val CHANNEL_ID = "DoralRemindersChannel"
        const val NOTIFICATION_ID_MORNING = 1001
        const val NOTIFICATION_ID_EVENING = 1002
    }

    override fun onReceive(context: Context, intent: Intent) {
        when (intent.action) {
            ReminderScheduler.ACTION_MORNING_REMINDER -> {
                showNotification(
                    context = context,
                    notificationId = NOTIFICATION_ID_MORNING,
                    title = "DORAL Fleet Control",
                    message = "¿Has registrado tu vehículo hoy?"
                )
                // Reprogramar para el día siguiente
                ReminderScheduler.scheduleDailyReminders(context)
            }

            ReminderScheduler.ACTION_EVENING_REMINDER -> {
                showNotification(
                    context = context,
                    notificationId = NOTIFICATION_ID_EVENING,
                    title = "DORAL Fleet Control",
                    message = "¿Has dejado el vehículo en la sede?"
                )
                // Reprogramar para el día siguiente
                ReminderScheduler.scheduleDailyReminders(context)
            }

            Intent.ACTION_BOOT_COMPLETED -> {
                // Al reiniciar el móvil, reprogramar los recordatorios
                ReminderScheduler.scheduleDailyReminders(context)
            }
        }
    }

    private fun showNotification(context: Context, notificationId: Int, title: String, message: String) {
        createNotificationChannel(context)

        val openAppIntent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
        }

        val pendingIntent = PendingIntent.getActivity(
            context,
            notificationId,
            openAppIntent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.logo_doral)
            .setContentTitle(title)
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .build()

        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as? NotificationManager
        notificationManager?.notify(notificationId, notification)
    }

    private fun createNotificationChannel(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "Recordatorios de Fichaje DORAL",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Notificaciones de recordatorio para entrada y salida de vehículos"
            }

            val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as? NotificationManager
            notificationManager?.createNotificationChannel(channel)
        }
    }
}

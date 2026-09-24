package com.example.doralfleetcontrol

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import java.util.Calendar

object ReminderScheduler {

    const val ACTION_MORNING_REMINDER = "com.example.doralfleetcontrol.ACTION_MORNING_REMINDER"
    const val ACTION_EVENING_REMINDER = "com.example.doralfleetcontrol.ACTION_EVENING_REMINDER"

    const val REQUEST_CODE_MORNING = 101
    const val REQUEST_CODE_EVENING = 102

    fun scheduleDailyReminders(context: Context) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as? AlarmManager ?: return

        // Programar recordatorio de mañana (07:00 AM)
        scheduleAlarm(
            context = context,
            alarmManager = alarmManager,
            hour = 7,
            minute = 0,
            action = ACTION_MORNING_REMINDER,
            requestCode = REQUEST_CODE_MORNING
        )

        // Programar recordatorio de tarde (15:50 PM)
        scheduleAlarm(
            context = context,
            alarmManager = alarmManager,
            hour = 15,
            minute = 50,
            action = ACTION_EVENING_REMINDER,
            requestCode = REQUEST_CODE_EVENING
        )
    }

    private fun scheduleAlarm(
        context: Context,
        alarmManager: AlarmManager,
        hour: Int,
        minute: Int,
        action: String,
        requestCode: Int
    ) {
        val intent = Intent(context, ReminderReceiver::class.java).apply {
            this.action = action
        }

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            requestCode,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        val calendar = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, hour)
            set(Calendar.MINUTE, minute)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)

            // Si la hora de hoy ya ha pasado, programar para mañana
            if (timeInMillis <= System.currentTimeMillis()) {
                add(Calendar.DAY_OF_YEAR, 1)
            }
        }

        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                alarmManager.setAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP,
                    calendar.timeInMillis,
                    pendingIntent
                )
            } else {
                alarmManager.set(
                    AlarmManager.RTC_WAKEUP,
                    calendar.timeInMillis,
                    pendingIntent
                )
            }
        } catch (e: SecurityException) {
            // Manejo defensivo en caso de restricciones estrictas del sistema
            alarmManager.setInexactRepeating(
                AlarmManager.RTC_WAKEUP,
                calendar.timeInMillis,
                AlarmManager.INTERVAL_DAY,
                pendingIntent
            )
        }
    }
}

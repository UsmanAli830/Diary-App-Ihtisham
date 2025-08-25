package com.example.diarywithlock.utils


import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.example.diarywithlock.notification.viewmodel.ReminderReceiver
import java.text.SimpleDateFormat
import java.util.*

fun convertToMillis(timeString: String): Long {
    val format = SimpleDateFormat("hh:mm a", Locale.getDefault())
    val parsedTime = format.parse(timeString) ?: return System.currentTimeMillis()

    val calendar = Calendar.getInstance().apply {
        timeInMillis = System.currentTimeMillis()

        val temp = Calendar.getInstance().apply { time = parsedTime }
        set(Calendar.HOUR_OF_DAY, temp.get(Calendar.HOUR_OF_DAY))
        set(Calendar.MINUTE, temp.get(Calendar.MINUTE))
        set(Calendar.SECOND, 0)
        set(Calendar.MILLISECOND, 0)

        if (timeInMillis < System.currentTimeMillis()) {
            add(Calendar.DAY_OF_YEAR, 1)
        }
    }

    return calendar.timeInMillis
}


fun scheduleReminder(context: Context, id: Int, timeMillis: Long) {
    val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S) {
        if (!alarmManager.canScheduleExactAlarms()) {
            // Ask user to allow exact alarms
            val intent = Intent("android.settings.REQUEST_SCHEDULE_EXACT_ALARM")
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            context.startActivity(intent)
            return
        }
    }

    val intent = Intent(context, ReminderReceiver::class.java).apply {
        putExtra("reminderId", id)
    }

    val pendingIntent = PendingIntent.getBroadcast(
        context, id, intent,
        PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
    )


    alarmManager.setExactAndAllowWhileIdle(
        AlarmManager.RTC_WAKEUP,
        timeMillis,
        pendingIntent
    )
}

fun cancelReminder(context: Context, id: Int) {
    val intent = Intent(context, ReminderReceiver::class.java).apply {
        putExtra("reminderId", id) // ✅ MATCH scheduleReminder
    }

    val pendingIntent = PendingIntent.getBroadcast(
        context, id, intent,
        PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
    )

    val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
    alarmManager.cancel(pendingIntent)
}


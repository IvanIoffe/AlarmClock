package com.ioffeivan.alarmclock.background.service.notification

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.ioffeivan.alarmclock.R
import com.ioffeivan.alarmclock.background.receiver.AlarmClockReceiver
import com.ioffeivan.alarmclock.core.utils.Action
import com.ioffeivan.alarmclock.core.utils.AlarmClockKeys

class AlarmClockNotificationActionHelper(
    private val context: Context,
) {
    fun snoozeAlarmClockAction(
        alarmClockId: Long,
    ): NotificationCompat.Action {
        val intent = Intent(context, AlarmClockReceiver::class.java).apply {
            putExtra(AlarmClockKeys.ID_KEY, alarmClockId)
            action = Action.ACTION_SNOOZE_ALARM_CLOCK
        }

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            alarmClockId.toInt(),
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        return NotificationCompat.Action.Builder(
            null,
            context.getString(R.string.snooze_alarm_clock),
            pendingIntent
        ).build()
    }

    fun stopAlarmClockAction(
        alarmClockId: Long,
    ): NotificationCompat.Action {
        val intent = Intent(context, AlarmClockReceiver::class.java).apply {
            putExtra(AlarmClockKeys.ID_KEY, alarmClockId)
            action = Action.ACTION_STOP_ALARM_CLOCK
        }

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            alarmClockId.toInt(),
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        return NotificationCompat.Action.Builder(
            null,
            context.getString(R.string.turn_off_alarm_clock),
            pendingIntent
        ).build()
    }
}
package com.ioffeivan.alarmclock.background.service.notification

import android.app.Notification
import android.content.Context
import androidx.core.app.NotificationCompat
import com.ioffeivan.alarmclock.R
import com.ioffeivan.alarmclock.core.utils.TimeFormatter
import com.ioffeivan.alarmclock.core.utils.getAlarmClockActionActivityPendingIntent
import java.time.LocalTime
import javax.inject.Inject

class AlarmClockNotificationHelper @Inject constructor(
    private val context: Context,
    private val alarmClockNotificationActionHelper: AlarmClockNotificationActionHelper,
) {
    fun createNotification(
        alarmClockId: Long,
        alarmClockName: String?,
        alarmClockTime: LocalTime,
    ): Notification {
        return NotificationCompat.Builder(context, AlarmClockNotificationChannel.NOTIFICATION_CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_alarm_clock)
            .setContentTitle(context.getString(R.string.alarm_clock))
            .setContentText(alarmClockName ?: TimeFormatter.getFormattedTime(alarmClockTime))
            .setCategory(NotificationCompat.CATEGORY_ALARM)
            .addAction(alarmClockNotificationActionHelper.snoozeAlarmClockAction(alarmClockId = alarmClockId))
            .addAction(alarmClockNotificationActionHelper.stopAlarmClockAction(alarmClockId = alarmClockId))
            .setFullScreenIntent(
                getAlarmClockActionActivityPendingIntent(
                    context = context,
                    alarmClockId = alarmClockId,
                    alarmClockName = alarmClockName,
                ), true
            )
            .build()
    }
}
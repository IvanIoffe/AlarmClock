package com.ioffeivan.alarmclock.background.service.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import com.ioffeivan.alarmclock.R

object AlarmClockNotificationChannel {
    const val NOTIFICATION_CHANNEL_ID = "alarm_clock_channel"

    fun createAlarmClockNotificationChannel(context: Context) {
        val channel = NotificationChannel(
            NOTIFICATION_CHANNEL_ID,
            context.getString(R.string.going_alarm_clock_notification_channel),
            NotificationManager.IMPORTANCE_HIGH,
        )

        val notificationManager: NotificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }
}
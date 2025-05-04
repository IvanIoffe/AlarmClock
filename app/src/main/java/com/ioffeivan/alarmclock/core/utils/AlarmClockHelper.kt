package com.ioffeivan.alarmclock.core.utils

import android.content.Context
import android.media.RingtoneManager
import android.net.Uri
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import com.ioffeivan.alarmclock.R
import java.time.Duration
import java.time.LocalTime

val defaultSound: Uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM)

fun getTimeMillisUntilAlarmClock(alarmClockTime: LocalTime): Long {
    val currentTime = LocalTime.now()

    val duration = if (currentTime > alarmClockTime) {
        Duration.between(currentTime, alarmClockTime).plusHours(24)
    } else {
        Duration.between(currentTime, alarmClockTime)
    }

    return duration.seconds * 1000
}

fun getNewAlarmClockTime(timeSnoozeInMinutes: Int): LocalTime {
    val currentTime = LocalTime.now()
    val currentTimeInMinutes = currentTime.hour * 60 + currentTime.minute

    val newTimeAlarmClockInMinutes = currentTimeInMinutes + timeSnoozeInMinutes
    val newHour = newTimeAlarmClockInMinutes / 60
    val newMinute = newTimeAlarmClockInMinutes - newHour * 60

    return LocalTime.of(newHour, newMinute)
}

fun showTimeUntilAlarmClockToast(context: Context, alarmClockTime: LocalTime) {
    val timeMillisUntilAlarmClock = getTimeMillisUntilAlarmClock(alarmClockTime)
    val formattedTime = PluralDurationFormatter(context).formatDuration(timeMillisUntilAlarmClock)
    val message = context.getString(R.string.alarm_clock_will_work_after, formattedTime)
    Handler(Looper.getMainLooper()).post {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }
}
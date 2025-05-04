package com.ioffeivan.alarmclock.core.utils

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.ioffeivan.alarmclock.core.presentation.AlarmClockActionActivity

fun getAlarmClockPendingIntent(
    context: Context,
    alarmClockId: Long?,
    intent: Intent,
): PendingIntent {
    return PendingIntent.getBroadcast(
        context,
        alarmClockId?.toInt() ?: -1,
        intent,
        PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
    )
}

fun getAlarmClockActionActivityPendingIntent(
    context: Context,
    alarmClockId: Long,
    alarmClockName: String?,
): PendingIntent {
    val intent = Intent(context, AlarmClockActionActivity::class.java).apply {
        putExtra(Constants.AlarmClockKeys.ALARM_CLOCK_ID_KEY, alarmClockId)
        putExtra(Constants.AlarmClockKeys.ALARM_CLOCK_NAME_KEY, alarmClockName)
    }

    return PendingIntent.getActivity(
        context,
        alarmClockId.toInt(),
        intent,
        PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
    )
}
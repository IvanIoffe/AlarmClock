package com.ioffeivan.alarmclock.background.scheduler

import android.app.AlarmManager
import android.content.Context
import android.content.Intent
import com.ioffeivan.alarmclock.background.receiver.AlarmClockReceiver
import com.ioffeivan.alarmclock.core.domain.model.AlarmClock
import com.ioffeivan.alarmclock.core.utils.Action
import com.ioffeivan.alarmclock.core.utils.AlarmClockKeys
import com.ioffeivan.alarmclock.core.utils.getAlarmClockPendingIntent
import com.ioffeivan.alarmclock.core.utils.getTimeMillisUntilAlarmClock
import javax.inject.Inject

class AlarmClockSchedulerImpl @Inject constructor(
    private val context: Context,
    private val alarmManager: AlarmManager,
) : AlarmClockScheduler {

    override fun schedule(alarmClock: AlarmClock) {
        val intent = createScheduleIntent(alarmClock)

        val alarmClockTime =
            System.currentTimeMillis() + getTimeMillisUntilAlarmClock(alarmClock.time)

        alarmManager.setAlarmClock(
            AlarmManager.AlarmClockInfo(alarmClockTime, null),
            getAlarmClockPendingIntent(context, alarmClock.id, intent)
        )
    }

    override fun cancel(alarmClock: AlarmClock) {
        val intent = createCancelIntent()
        alarmManager.cancel(getAlarmClockPendingIntent(context, alarmClock.id, intent))
    }

    private fun createScheduleIntent(alarmClock: AlarmClock): Intent {
        return Intent(context, AlarmClockReceiver::class.java).apply {
            action = Action.ACTION_START_OR_CANCEL_ALARM_CLOCK
            putExtra(AlarmClockKeys.ID_KEY, alarmClock.id)
            putExtra(AlarmClockKeys.HOUR_KEY, alarmClock.time.hour.toString())
            putExtra(AlarmClockKeys.MINUTE_KEY, alarmClock.time.minute.toString())
            putExtra(AlarmClockKeys.SOUND_TYPE_KEY, alarmClock.sound.type.name)
            putExtra(AlarmClockKeys.SOUND_URI_KEY, alarmClock.sound.uri)
            putExtra(AlarmClockKeys.IS_VIBRATE_KEY, alarmClock.isVibrate)
            putExtra(AlarmClockKeys.NAME_KEY, alarmClock.name)
        }
    }

    private fun createCancelIntent(): Intent {
        return Intent(context, AlarmClockReceiver::class.java).apply {
            action = Action.ACTION_START_OR_CANCEL_ALARM_CLOCK
        }
    }
}
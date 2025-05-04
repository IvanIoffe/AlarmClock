package com.ioffeivan.alarmclock.background.scheduler

import android.app.AlarmManager
import android.content.Context
import android.content.Intent
import com.ioffeivan.alarmclock.background.receiver.AlarmClockReceiver
import com.ioffeivan.alarmclock.core.domain.model.AlarmClock
import com.ioffeivan.alarmclock.core.utils.Constants
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
            action = Constants.Action.ACTION_START_OR_CANCEL_ALARM_CLOCK
            putExtra(Constants.AlarmClockKeys.ALARM_CLOCK_ID_KEY, alarmClock.id)
            putExtra(Constants.AlarmClockKeys.ALARM_CLOCK_SOUND_TYPE_KEY, alarmClock.sound.type.name)
            putExtra(Constants.AlarmClockKeys.ALARM_CLOCK_SOUND_URI_KEY, alarmClock.sound.uri)
            putExtra(Constants.AlarmClockKeys.ALARM_CLOCK_IS_VIBRATE_KEY, alarmClock.isVibrate)
            putExtra(Constants.AlarmClockKeys.ALARM_CLOCK_NAME_KEY, alarmClock.name)
        }
    }

    private fun createCancelIntent(): Intent {
        return Intent(context, AlarmClockReceiver::class.java).apply {
            action = Constants.Action.ACTION_START_OR_CANCEL_ALARM_CLOCK
        }
    }
}
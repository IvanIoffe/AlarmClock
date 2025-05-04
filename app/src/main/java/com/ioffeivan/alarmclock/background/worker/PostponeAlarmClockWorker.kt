package com.ioffeivan.alarmclock.background.worker

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.Data
import androidx.work.WorkerParameters
import com.ioffeivan.alarmclock.core.domain.usecase.ScheduleAlarmClockUseCase
import com.ioffeivan.alarmclock.core.utils.Constants
import com.ioffeivan.alarmclock.core.utils.getNewAlarmClockTime
import com.ioffeivan.alarmclock.core.utils.showTimeUntilAlarmClockToast
import com.ioffeivan.alarmclock.create_and_update_alarmclock.domain.usecase.GetAlarmClockByIdUseCase
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

@HiltWorker
class SnoozeAlarmClockWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted params: WorkerParameters,
    private val getAlarmClockByIdUseCase: GetAlarmClockByIdUseCase,
    private val scheduleAlarmClockUseCase: ScheduleAlarmClockUseCase,
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        return try {
            val alarmClockId = inputData.getLong(Constants.AlarmClockKeys.ALARM_CLOCK_ID_KEY, -1)
            val alarmClock = getAlarmClockByIdUseCase(alarmClockId)

            val newTimeAlarmClock =
                getNewAlarmClockTime(timeSnoozeInMinutes = alarmClock.timeSnooze)
            scheduleAlarmClockUseCase(alarmClock.copy(time = newTimeAlarmClock))

            val outputData = Data.Builder()
                .putInt(Constants.AlarmClockKeys.ALARM_CLOCK_HOUR_KEY, newTimeAlarmClock.hour)
                .putInt(Constants.AlarmClockKeys.ALARM_CLOCK_MINUTE_KEY, newTimeAlarmClock.minute)
                .putBoolean(Constants.AlarmClockKeys.ALARM_CLOCK_IS_ENABLED_KEY, true)
                .build()

            showTimeUntilAlarmClockToast(
                context = applicationContext,
                alarmClockTime = newTimeAlarmClock,
            )

            Result.success(outputData)
        } catch (e: Exception) {
            Result.failure()
        }
    }
}
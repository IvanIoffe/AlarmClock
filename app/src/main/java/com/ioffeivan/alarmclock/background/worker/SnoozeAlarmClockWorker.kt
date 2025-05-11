package com.ioffeivan.alarmclock.background.worker

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.Data
import androidx.work.WorkerParameters
import com.ioffeivan.alarmclock.background.worker.utils.AlarmClockInputDataUtils
import com.ioffeivan.alarmclock.core.domain.usecase.ScheduleAlarmClockUseCase
import com.ioffeivan.alarmclock.core.utils.AlarmClockKeys
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
            val alarmClockInputDataUtils = AlarmClockInputDataUtils(inputData)

            val alarmClockId = alarmClockInputDataUtils.getId() ?: return Result.failure()
            val alarmClock = getAlarmClockByIdUseCase(alarmClockId)

            val newTimeAlarmClock =
                getNewAlarmClockTime(timeSnoozeInMinutes = alarmClock.timeSnooze)
            scheduleAlarmClockUseCase(alarmClock.copy(time = newTimeAlarmClock))

            val outputData = Data.Builder()
                .putInt(AlarmClockKeys.HOUR_KEY, newTimeAlarmClock.hour)
                .putInt(AlarmClockKeys.MINUTE_KEY, newTimeAlarmClock.minute)
                .putBoolean(AlarmClockKeys.IS_ENABLED_KEY, true)
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
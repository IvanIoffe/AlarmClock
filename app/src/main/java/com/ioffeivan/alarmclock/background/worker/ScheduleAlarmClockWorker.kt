package com.ioffeivan.alarmclock.background.worker

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.Data
import androidx.work.WorkerParameters
import com.ioffeivan.alarmclock.core.domain.usecase.ScheduleAlarmClockUseCase
import com.ioffeivan.alarmclock.core.utils.Constants
import com.ioffeivan.alarmclock.create_and_update_alarmclock.domain.usecase.GetAlarmClockByIdUseCase
import com.ioffeivan.alarmclock.sound_selection.domain.model.SoundType
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import java.time.LocalTime

@HiltWorker
class ScheduleAlarmClockWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted params: WorkerParameters,
    private val getAlarmClockByIdUseCase: GetAlarmClockByIdUseCase,
    private val scheduleAlarmClockUseCase: ScheduleAlarmClockUseCase,
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        return try {
            val alarmClockId = inputData.getLong(Constants.AlarmClockKeys.ALARM_CLOCK_ID_KEY, -1)
            val alarmClock = getAlarmClockByIdUseCase(alarmClockId)

            val hour = inputData.getInt(Constants.AlarmClockKeys.ALARM_CLOCK_HOUR_KEY, alarmClock.time.hour)

            val minute = inputData.getInt(Constants.AlarmClockKeys.ALARM_CLOCK_MINUTE_KEY, alarmClock.time.minute)

            val soundName =
                inputData.getString(Constants.AlarmClockKeys.ALARM_CLOCK_SOUND_NAME_KEY) ?: alarmClock.sound.name

            val soundUri =
                inputData.getString(Constants.AlarmClockKeys.ALARM_CLOCK_SOUND_URI_KEY) ?: alarmClock.sound.uri

            val soundType = inputData.getString(Constants.AlarmClockKeys.ALARM_CLOCK_SOUND_TYPE_KEY)
                ?: alarmClock.sound.type.name

            scheduleAlarmClockUseCase(
                alarmClock.copy(
                    time = LocalTime.of(hour, minute),
                    sound = alarmClock.sound.copy(
                        name = soundName,
                        type = SoundType.valueOf(soundType),
                        uri = soundUri,
                    )
                )
            )

            val outputData = Data.Builder()
                .putBoolean(Constants.AlarmClockKeys.ALARM_CLOCK_IS_ENABLED_KEY, true)
                .putString(Constants.AlarmClockKeys.ALARM_CLOCK_SOUND_NAME_KEY, soundName)
                .putString(Constants.AlarmClockKeys.ALARM_CLOCK_SOUND_URI_KEY, soundUri)
                .putString(Constants.AlarmClockKeys.ALARM_CLOCK_SOUND_TYPE_KEY, soundType)
                .build()

            Result.success(outputData)
        } catch (e: Exception) {
            Result.failure()
        }
    }
}
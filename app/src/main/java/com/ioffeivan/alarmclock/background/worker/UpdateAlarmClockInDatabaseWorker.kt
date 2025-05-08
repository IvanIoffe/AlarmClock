package com.ioffeivan.alarmclock.background.worker

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.ioffeivan.alarmclock.background.worker.utils.AlarmClockInputDataUtils
import com.ioffeivan.alarmclock.create_and_update_alarmclock.domain.usecase.GetAlarmClockByIdUseCase
import com.ioffeivan.alarmclock.create_and_update_alarmclock.domain.usecase.UpdateAlarmClockUseCase
import com.ioffeivan.alarmclock.sound_selection.domain.model.Sound
import com.ioffeivan.alarmclock.sound_selection.domain.model.SoundType
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import java.time.LocalTime

@HiltWorker
class UpdateAlarmClockInDatabaseWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted params: WorkerParameters,
    private val getAlarmClockByIdUseCase: GetAlarmClockByIdUseCase,
    private val updateAlarmClockUseCase: UpdateAlarmClockUseCase,
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        return try {
            val alarmClockInputDataUtils = AlarmClockInputDataUtils(inputData)

            val alarmClockId = alarmClockInputDataUtils.getId() ?: return Result.failure()
            val alarmClock = getAlarmClockByIdUseCase(alarmClockId)

            val hour = alarmClockInputDataUtils.getHour(alarmClock.time.hour)
            val minute = alarmClockInputDataUtils.getMinute(alarmClock.time.minute)
            val isEnabled = alarmClockInputDataUtils.isEnabled(false)
            val soundName = alarmClockInputDataUtils.getSoundName(alarmClock.sound.name)
            val soundUri = alarmClockInputDataUtils.getSoundUri(alarmClock.sound.uri)
            val soundType = alarmClockInputDataUtils.getSoundType(alarmClock.sound.type.name)

            updateAlarmClockUseCase(
                alarmClock.copy(
                    time = LocalTime.of(hour, minute),
                    isEnabled = isEnabled,
                    sound = Sound(
                        name = soundName,
                        type = SoundType.valueOf(soundType),
                        uri = soundUri,
                    ),
                )
            )

            Result.success()
        } catch (e: Exception) {
            Result.failure()
        }
    }
}
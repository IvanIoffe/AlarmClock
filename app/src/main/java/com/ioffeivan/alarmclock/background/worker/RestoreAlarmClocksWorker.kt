package com.ioffeivan.alarmclock.background.worker

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.ioffeivan.alarmclock.background.scheduler.AlarmClockScheduler
import com.ioffeivan.alarmclock.home.domain.usecase.GetAllAlarmClocksUseCase
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.take

@HiltWorker
class RestoreAlarmClocksWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted params: WorkerParameters,
    private val getAllAlarmClocksUseCase: GetAllAlarmClocksUseCase,
    private val alarmClockScheduler: AlarmClockScheduler
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        return try {
            getAllAlarmClocksUseCase()
                .take(1)
                .collect { alarmClocks ->
                    alarmClocks.items
                        .filter { it.isEnabled }
                        .forEach { alarmClock ->
                            alarmClockScheduler.schedule(alarmClock)
                        }
                }

            Result.success()
        } catch (e: Exception) {
            Result.retry()
        }
    }
}
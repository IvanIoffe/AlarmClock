package com.ioffeivan.alarmclock.core.domain.usecase

import com.ioffeivan.alarmclock.background.scheduler.AlarmClockScheduler
import com.ioffeivan.alarmclock.core.domain.model.AlarmClock
import javax.inject.Inject

class ScheduleAlarmClockUseCase @Inject constructor(
    private val alarmClockScheduler: AlarmClockScheduler,
) {
    operator fun invoke(alarmClock: AlarmClock) {
        alarmClockScheduler.schedule(alarmClock)
    }
}
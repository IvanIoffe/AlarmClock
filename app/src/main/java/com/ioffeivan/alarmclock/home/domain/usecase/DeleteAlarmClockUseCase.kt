package com.ioffeivan.alarmclock.home.domain.usecase

import com.ioffeivan.alarmclock.core.domain.model.AlarmClock
import com.ioffeivan.alarmclock.core.domain.repository.AlarmClockRepository
import javax.inject.Inject

class DeleteAlarmClockUseCase @Inject constructor(
    private val alarmClockRepository: AlarmClockRepository
) {
    suspend operator fun invoke(alarmClock: AlarmClock) = alarmClockRepository.deleteAlarmClock(alarmClock)
}
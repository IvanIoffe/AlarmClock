package com.ioffeivan.alarmclock.create_and_update_alarmclock.domain.usecase

import com.ioffeivan.alarmclock.core.domain.model.AlarmClock
import com.ioffeivan.alarmclock.core.domain.repository.AlarmClockRepository
import javax.inject.Inject

class AddAlarmClockUseCase @Inject constructor(
    private val alarmClockRepository: AlarmClockRepository
) {
    suspend operator fun invoke(alarmClock: AlarmClock) = alarmClockRepository.addAlarmClock(alarmClock)
}
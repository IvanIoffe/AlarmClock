package com.ioffeivan.alarmclock.home.domain.usecase

import com.ioffeivan.alarmclock.core.domain.repository.AlarmClockRepository
import javax.inject.Inject

class GetAllAlarmClocksUseCase @Inject constructor(
    private val alarmClockRepository: AlarmClockRepository
) {
    operator fun invoke() = alarmClockRepository.getAllAlarmClocks()
}
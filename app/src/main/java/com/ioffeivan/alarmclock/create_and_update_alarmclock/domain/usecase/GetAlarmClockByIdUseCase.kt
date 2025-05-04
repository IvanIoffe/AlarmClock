package com.ioffeivan.alarmclock.create_and_update_alarmclock.domain.usecase

import com.ioffeivan.alarmclock.core.domain.repository.AlarmClockRepository
import javax.inject.Inject

class GetAlarmClockByIdUseCase @Inject constructor(
    private val alarmClockRepository: AlarmClockRepository
) {
    suspend operator fun invoke(id: Long) = alarmClockRepository.getAlarmClockById(id)
}
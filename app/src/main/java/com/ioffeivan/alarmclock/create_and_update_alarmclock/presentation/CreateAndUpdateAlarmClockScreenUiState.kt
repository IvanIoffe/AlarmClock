package com.ioffeivan.alarmclock.create_and_update_alarmclock.presentation

import com.ioffeivan.alarmclock.core.domain.model.AlarmClock

sealed class CreateAndUpdateAlarmClockScreenUiState {
    data object Initial: CreateAndUpdateAlarmClockScreenUiState()

    data class Content(
        val alarmClock: AlarmClock,
    ) : CreateAndUpdateAlarmClockScreenUiState()
}
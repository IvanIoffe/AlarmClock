package com.ioffeivan.alarmclock.home.presentation

import com.ioffeivan.alarmclock.core.domain.model.AlarmClocks

sealed class HomeScreenUiState {
    data object Empty : HomeScreenUiState()
    data object Initial: HomeScreenUiState()

    data class Content(
        val alarmClocks: AlarmClocks = AlarmClocks(items = listOf())
    ) : HomeScreenUiState()
}
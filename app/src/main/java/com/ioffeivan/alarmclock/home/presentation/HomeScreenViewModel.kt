package com.ioffeivan.alarmclock.home.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ioffeivan.alarmclock.core.domain.model.AlarmClock
import com.ioffeivan.alarmclock.core.domain.usecase.CancelAlarmClockUseCase
import com.ioffeivan.alarmclock.core.domain.usecase.CancelGetNewReleaseSpotifyArtistUseCase
import com.ioffeivan.alarmclock.core.domain.usecase.ScheduleAlarmClockUseCase
import com.ioffeivan.alarmclock.core.domain.usecase.ScheduleGetNewReleaseSpotifyArtistUseCase
import com.ioffeivan.alarmclock.create_and_update_alarmclock.domain.usecase.UpdateAlarmClockUseCase
import com.ioffeivan.alarmclock.home.domain.usecase.DeleteAlarmClockUseCase
import com.ioffeivan.alarmclock.home.domain.usecase.GetAllAlarmClocksUseCase
import com.ioffeivan.alarmclock.sound_selection.domain.model.SoundType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeScreenViewModel @Inject constructor(
    private val getAllAlarmClocksUseCase: GetAllAlarmClocksUseCase,
    private val updateAlarmClockUseCase: UpdateAlarmClockUseCase,
    private val deleteAlarmClockUseCase: DeleteAlarmClockUseCase,
    private val scheduleAlarmClockUseCase: ScheduleAlarmClockUseCase,
    private val cancelAlarmClockUseCase: CancelAlarmClockUseCase,
    private val scheduleGetNewReleaseSpotifyArtistUseCase: ScheduleGetNewReleaseSpotifyArtistUseCase,
    private val cancelGetNewReleaseSpotifyArtistUseCase: CancelGetNewReleaseSpotifyArtistUseCase,
) : ViewModel() {
    private val _uiState = MutableStateFlow<HomeScreenUiState>(HomeScreenUiState.Initial)
    val uiState: StateFlow<HomeScreenUiState> = _uiState.asStateFlow()

    init {
        getAllAlarmClocks()
    }

    private fun getAllAlarmClocks() {
        getAllAlarmClocksUseCase()
            .onEach { alarmClocks ->
                _uiState.value = if (alarmClocks.items.isNotEmpty()) {
                    HomeScreenUiState.Content(alarmClocks)
                } else {
                    HomeScreenUiState.Empty
                }
            }
            .launchIn(viewModelScope)
    }

    fun updateAlarmClock(alarmClock: AlarmClock) {
        viewModelScope.launch(Dispatchers.IO) {
            updateAlarmClockUseCase(alarmClock.copy(isEnabled = !alarmClock.isEnabled))
        }

        if (alarmClock.isEnabled) {
            cancelAlarmClock(alarmClock)
        } else {
            scheduleAlarmClock(alarmClock)
        }
    }

    fun deleteAlarmClock(alarmClock: AlarmClock) {
        viewModelScope.launch(Dispatchers.IO) {
            deleteAlarmClockUseCase(alarmClock)
        }
        if (alarmClock.isEnabled) {
            cancelAlarmClock(alarmClock)
        }
    }

    private fun cancelAlarmClock(alarmClock: AlarmClock) {
        cancelAlarmClockUseCase(alarmClock)
        if (alarmClock.sound.type == SoundType.NEW_RELEASE_SPOTIFY_ARTIST) {
            cancelGetNewReleaseSpotifyArtistUseCase(alarmClock)
        }
    }

    private fun scheduleAlarmClock(alarmClock: AlarmClock) {
        scheduleAlarmClockUseCase(alarmClock)
        if (alarmClock.sound.type == SoundType.NEW_RELEASE_SPOTIFY_ARTIST) {
            scheduleGetNewReleaseSpotifyArtistUseCase(alarmClock)
        }
    }
}

package com.ioffeivan.alarmclock.create_and_update_alarmclock.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ioffeivan.alarmclock.core.domain.model.AlarmClock
import com.ioffeivan.alarmclock.core.domain.usecase.ScheduleAlarmClockUseCase
import com.ioffeivan.alarmclock.core.domain.usecase.ScheduleGetNewReleaseSpotifyArtistUseCase
import com.ioffeivan.alarmclock.core.utils.defaultSound
import com.ioffeivan.alarmclock.create_and_update_alarmclock.domain.usecase.AddAlarmClockUseCase
import com.ioffeivan.alarmclock.create_and_update_alarmclock.domain.usecase.GetAlarmClockByIdUseCase
import com.ioffeivan.alarmclock.create_and_update_alarmclock.domain.usecase.UpdateAlarmClockUseCase
import com.ioffeivan.alarmclock.sound_selection.domain.model.Sound
import com.ioffeivan.alarmclock.sound_selection.domain.model.SoundType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class CreateAndUpdateAlarmClockScreenViewModel @Inject constructor(
    private val addAlarmClockUseCase: AddAlarmClockUseCase,
    private val getAlarmClockByIdUseCase: GetAlarmClockByIdUseCase,
    private val updateAlarmClockUseCase: UpdateAlarmClockUseCase,
    private val scheduleAlarmClockUseCase: ScheduleAlarmClockUseCase,
    private val scheduleGetNewReleaseSpotifyArtistUseCase: ScheduleGetNewReleaseSpotifyArtistUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow<CreateAndUpdateAlarmClockScreenUiState>(
        CreateAndUpdateAlarmClockScreenUiState.Initial
    )
    val uiState: StateFlow<CreateAndUpdateAlarmClockScreenUiState> = _uiState.asStateFlow()

    fun getAlarmClockById(id: String?) {
        viewModelScope.launch(Dispatchers.IO) {
            _uiState.value = if (id != null) {
                CreateAndUpdateAlarmClockScreenUiState.Content(
                    getAlarmClockByIdUseCase(id.toLong())
                )
            } else {
                CreateAndUpdateAlarmClockScreenUiState.Content(
                    AlarmClock(
                        sound = Sound(
                            name = "Default sound",
                            type = SoundType.SYSTEM,
                            uri = defaultSound.toString(),
                        ),
                    )
                )
            }
        }
    }

    fun addOrUpdateAlarmClock(_alarmClock: AlarmClock) {
        viewModelScope.launch(Dispatchers.IO) {
            val alarmClock = if (_alarmClock.id != null) {
                updateAlarmClock(_alarmClock)
                _alarmClock
            } else {
                addAlarmClock(_alarmClock)
            }
            withContext(Dispatchers.Main) {
                scheduleAlarmClock(alarmClock)
            }
        }
    }

    private suspend fun updateAlarmClock(alarmClock: AlarmClock) {
        updateAlarmClockUseCase(alarmClock)
    }

    private suspend fun addAlarmClock(alarmClock: AlarmClock): AlarmClock {
        val alarmClockId = addAlarmClockUseCase(alarmClock)
        return alarmClock.copy(id = alarmClockId)
    }

    private fun scheduleAlarmClock(alarmClock: AlarmClock) {
        scheduleAlarmClockUseCase(alarmClock)
        if (alarmClock.sound.type == SoundType.NEW_RELEASE_SPOTIFY_ARTIST) {
            scheduleGetNewReleaseSpotifyArtistUseCase(alarmClock)
        }
    }
}
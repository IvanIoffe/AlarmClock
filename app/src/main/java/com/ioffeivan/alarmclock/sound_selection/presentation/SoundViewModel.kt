package com.ioffeivan.alarmclock.sound_selection.presentation

import androidx.lifecycle.ViewModel
import com.ioffeivan.alarmclock.sound_selection.domain.model.Sound
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class SoundViewModel : ViewModel() {
    private val _sound = MutableStateFlow<Sound?>(null)
    val sound: StateFlow<Sound?> = _sound.asStateFlow()

    fun setSound(sound: Sound) {
        _sound.value = sound
    }
}
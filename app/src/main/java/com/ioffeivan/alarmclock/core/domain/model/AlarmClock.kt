package com.ioffeivan.alarmclock.core.domain.model

import com.ioffeivan.alarmclock.sound_selection.domain.model.Sound
import java.time.LocalTime

data class AlarmClock(
    val id: Long? = null,
    val time: LocalTime = LocalTime.now(),
    val isEnabled: Boolean = false,
    val sound: Sound = Sound(),
    val isVibrate: Boolean = false,
    val name: String? = null,
    val timeSnooze: Int = 5,
)
package com.ioffeivan.alarmclock.sound_selection.domain.model

data class Sound(
    val name: String? = null,
    val type: SoundType = SoundType.SYSTEM,
    val uri: String? = null,
)
package com.ioffeivan.alarmclock.sound_selection.data.mapper

import com.ioffeivan.alarmclock.sound_selection.data.model.SoundLocal
import com.ioffeivan.alarmclock.sound_selection.domain.model.Sound
import com.ioffeivan.alarmclock.sound_selection.domain.model.SoundType

fun Sound.toSoundLocal(): SoundLocal =
    SoundLocal(
        name = name,
        type = type.name,
        uri = uri,
    )

fun SoundLocal.toSound() =
    Sound(
        name = name,
        type = SoundType.valueOf(type),
        uri = uri,
    )
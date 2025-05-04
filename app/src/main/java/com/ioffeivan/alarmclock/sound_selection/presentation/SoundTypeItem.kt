package com.ioffeivan.alarmclock.sound_selection.presentation

import androidx.compose.ui.graphics.painter.Painter
import com.ioffeivan.alarmclock.sound_selection.domain.model.SoundType

data class SoundTypeItem(
    val icon: Painter,
    val type: SoundType,
    val name: String,
)
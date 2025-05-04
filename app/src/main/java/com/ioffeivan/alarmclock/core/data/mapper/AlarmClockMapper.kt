package com.ioffeivan.alarmclock.core.data.mapper

import com.ioffeivan.alarmclock.core.data.source.local.model.AlarmClockLocal
import com.ioffeivan.alarmclock.core.domain.model.AlarmClock
import com.ioffeivan.alarmclock.core.domain.model.AlarmClocks
import com.ioffeivan.alarmclock.sound_selection.data.mapper.toSound
import com.ioffeivan.alarmclock.sound_selection.data.mapper.toSoundLocal
import java.time.LocalTime

fun List<AlarmClockLocal>.toAlarmClocks() =
    AlarmClocks(
        items = this.map { it.toAlarmClock() },
    )

fun AlarmClockLocal.toAlarmClock() =
    AlarmClock(
        id = id,
        time = LocalTime.of(hour, minute),
        isEnabled = isEnabled,
        sound = sound.toSound(),
        isVibrate = isVibrate,
        name = name,
        timeSnooze = timeSnooze,
    )

fun AlarmClock.toAlarmClockLocal() =
    AlarmClockLocal(
        id = id,
        hour = time.hour,
        minute = time.minute,
        isEnabled = isEnabled,
        sound = sound.toSoundLocal(),
        isVibrate = isVibrate,
        name = name,
        timeSnooze = timeSnooze,
    )
package com.ioffeivan.alarmclock.core.data.source.local.model

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.ioffeivan.alarmclock.sound_selection.data.model.SoundLocal

@Entity(tableName = "alarm_clocks")
data class AlarmClockLocal(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo("id")
    val id: Long?,

    @ColumnInfo("hour")
    val hour: Int,

    @ColumnInfo("minute")
    val minute: Int,

    @ColumnInfo("is_enabled")
    val isEnabled: Boolean,

    @Embedded(prefix = "sound_")
    val sound: SoundLocal,

    @ColumnInfo("is_vibrate")
    val isVibrate: Boolean,

    @ColumnInfo("name")
    val name: String?,

    @ColumnInfo("time_snooze")
    val timeSnooze: Int,
)
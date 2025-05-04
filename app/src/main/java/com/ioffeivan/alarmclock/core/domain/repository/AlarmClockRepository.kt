package com.ioffeivan.alarmclock.core.domain.repository

import com.ioffeivan.alarmclock.core.domain.model.AlarmClock
import com.ioffeivan.alarmclock.core.domain.model.AlarmClocks
import kotlinx.coroutines.flow.Flow

interface AlarmClockRepository {
    fun getAllAlarmClocks(): Flow<AlarmClocks>

    suspend fun getAlarmClockById(id: Long): AlarmClock

    suspend fun addAlarmClock(alarmClock: AlarmClock): Long

    suspend fun updateAlarmClock(alarmClock: AlarmClock)

    suspend fun deleteAlarmClock(alarmClock: AlarmClock)
}
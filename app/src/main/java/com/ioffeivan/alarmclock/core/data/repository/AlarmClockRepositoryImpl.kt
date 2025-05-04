package com.ioffeivan.alarmclock.core.data.repository

import com.ioffeivan.alarmclock.core.data.source.local.AlarmClockLocalDataSource
import com.ioffeivan.alarmclock.core.domain.model.AlarmClock
import com.ioffeivan.alarmclock.core.domain.model.AlarmClocks
import com.ioffeivan.alarmclock.core.domain.repository.AlarmClockRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class AlarmClockRepositoryImpl @Inject constructor(
    private val alarmClockLocalDataSource: AlarmClockLocalDataSource
) : AlarmClockRepository {

    override fun getAllAlarmClocks(): Flow<AlarmClocks> {
        return alarmClockLocalDataSource.getAllAlarmClocks()
    }

    override suspend fun getAlarmClockById(id: Long): AlarmClock {
        return alarmClockLocalDataSource.getAlarmClockById(id)
    }

    override suspend fun addAlarmClock(alarmClock: AlarmClock): Long {
        return alarmClockLocalDataSource.addAlarmClock(alarmClock)
    }

    override suspend fun updateAlarmClock(alarmClock: AlarmClock) {
        alarmClockLocalDataSource.updateAlarmClock(alarmClock)
    }

    override suspend fun deleteAlarmClock(alarmClock: AlarmClock) {
        alarmClockLocalDataSource.deleteAlarmClock(alarmClock)
    }
}
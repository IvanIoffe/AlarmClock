package com.ioffeivan.alarmclock.core.data.source.local

import com.ioffeivan.alarmclock.core.data.mapper.toAlarmClock
import com.ioffeivan.alarmclock.core.data.mapper.toAlarmClockLocal
import com.ioffeivan.alarmclock.core.data.mapper.toAlarmClocks
import com.ioffeivan.alarmclock.core.data.source.local.dao.AlarmClockDao
import com.ioffeivan.alarmclock.core.domain.model.AlarmClock
import com.ioffeivan.alarmclock.core.domain.model.AlarmClocks
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class RoomAlarmClockLocalDataSource @Inject constructor(
    private val alarmClockDao: AlarmClockDao
) : AlarmClockLocalDataSource {

    override fun getAllAlarmClocks(): Flow<AlarmClocks> {
        return alarmClockDao.getAllAlarmClocks()
            .map {
                it.toAlarmClocks()
            }
    }

    override suspend fun getAlarmClockById(id: Long): AlarmClock {
        return alarmClockDao.getAlarmClockById(id).toAlarmClock()
    }

    override suspend fun addAlarmClock(alarmClock: AlarmClock): Long {
        return alarmClockDao.addAlarmClock(alarmClock.toAlarmClockLocal())
    }

    override suspend fun updateAlarmClock(alarmClock: AlarmClock) {
        alarmClockDao.updateAlarmClock(alarmClock.toAlarmClockLocal())
    }

    override suspend fun deleteAlarmClock(alarmClock: AlarmClock) {
        alarmClockDao.deleteAlarmClock(alarmClock.toAlarmClockLocal())
    }
}
package com.ioffeivan.alarmclock.core.data.source.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.ioffeivan.alarmclock.core.data.source.local.model.AlarmClockLocal
import kotlinx.coroutines.flow.Flow

@Dao
interface AlarmClockDao {

    @Query("SELECT * FROM alarm_clocks ORDER BY hour, minute")
    fun getAllAlarmClocks(): Flow<List<AlarmClockLocal>>

    @Query("SELECT * FROM alarm_clocks WHERE id = :id")
    suspend fun getAlarmClockById(id: Long): AlarmClockLocal

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addAlarmClock(alarmClockLocal: AlarmClockLocal): Long

    @Update
    suspend fun updateAlarmClock(alarmClockLocal: AlarmClockLocal)

    @Delete
    suspend fun deleteAlarmClock(alarmClockLocal: AlarmClockLocal)
}
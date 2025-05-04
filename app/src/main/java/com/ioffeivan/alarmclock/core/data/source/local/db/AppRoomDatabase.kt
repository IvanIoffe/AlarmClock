package com.ioffeivan.alarmclock.core.data.source.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.ioffeivan.alarmclock.core.data.source.local.dao.AlarmClockDao
import com.ioffeivan.alarmclock.core.data.source.local.model.AlarmClockLocal

@Database(
    entities = [AlarmClockLocal::class],
    version = 160,
    exportSchema = false,
)
abstract class AppRoomDatabase : RoomDatabase() {
    abstract fun alarmClockDao(): AlarmClockDao
}
package com.ioffeivan.alarmclock.core.data.source.local.di

import com.ioffeivan.alarmclock.core.data.repository.AlarmClockRepositoryImpl
import com.ioffeivan.alarmclock.core.data.source.local.AlarmClockLocalDataSource
import com.ioffeivan.alarmclock.core.data.source.local.RoomAlarmClockLocalDataSource
import com.ioffeivan.alarmclock.core.data.source.local.dao.AlarmClockDao
import com.ioffeivan.alarmclock.core.data.source.local.db.AppRoomDatabase
import com.ioffeivan.alarmclock.core.domain.repository.AlarmClockRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class AlarmClockLocalSourceModuleProvider {

    @Singleton
    @Provides
    fun provideAlarmClockDao(database: AppRoomDatabase): AlarmClockDao = database.alarmClockDao()
}


@InstallIn(SingletonComponent::class)
@Module
interface AlarmClockLocalSourceModuleBinder {

    @Binds
    fun bindRoomLocalDataSource(roomLocalDataSource: RoomAlarmClockLocalDataSource): AlarmClockLocalDataSource

    @Binds
    fun bindAlarmClockRepositoryImpl(alarmClockRepositoryImpl: AlarmClockRepositoryImpl): AlarmClockRepository
}
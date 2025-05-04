package com.ioffeivan.alarmclock.background.di

import android.app.AlarmManager
import android.content.Context
import androidx.work.WorkManager
import com.ioffeivan.alarmclock.background.scheduler.AlarmClockScheduler
import com.ioffeivan.alarmclock.background.scheduler.AlarmClockSchedulerImpl
import com.ioffeivan.alarmclock.background.scheduler.GetNewReleaseSpotifyArtistScheduler
import com.ioffeivan.alarmclock.background.scheduler.GetNewReleaseSpotifyArtistSchedulerImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class BackgroundModuleProvider {

    @Singleton
    @Provides
    fun provideAlarmManager(
        context: Context
    ) = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

    @Singleton
    @Provides
    fun provideWorkManager(context: Context) = WorkManager.getInstance(context)
}

@InstallIn(SingletonComponent::class)
@Module
interface BackgroundModuleBinder {

    @Binds
    fun bindAlarmClockSchedulerImpl(
        alarmClockSchedulerImpl: AlarmClockSchedulerImpl,
    ): AlarmClockScheduler

    @Binds
    fun bindGetNewReleaseSpotifyArtistSchedulerImpl(
        getNewReleaseSpotifyArtistSchedulerImpl: GetNewReleaseSpotifyArtistSchedulerImpl,
    ): GetNewReleaseSpotifyArtistScheduler
}
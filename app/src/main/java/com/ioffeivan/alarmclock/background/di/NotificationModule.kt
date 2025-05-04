package com.ioffeivan.alarmclock.background.di

import android.content.Context
import com.ioffeivan.alarmclock.background.service.notification.AlarmClockNotificationActionHelper
import com.ioffeivan.alarmclock.background.service.notification.AlarmClockNotificationHelper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ServiceComponent
import dagger.hilt.android.scopes.ServiceScoped

@InstallIn(ServiceComponent::class)
@Module
class NotificationModule {

    @ServiceScoped
    @Provides
    fun provideAlarmClockNotificationHelper(
        context: Context,
        alarmClockNotificationActionHelper: AlarmClockNotificationActionHelper,
    ) = AlarmClockNotificationHelper(
        context = context,
        alarmClockNotificationActionHelper = alarmClockNotificationActionHelper,
    )

    @ServiceScoped
    @Provides
    fun provideAlarmClockNotificationActionHelper(
        context: Context,
    ) = AlarmClockNotificationActionHelper(context = context)
}
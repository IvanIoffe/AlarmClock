package com.ioffeivan.alarmclock.app.di

import android.content.Context
import androidx.room.Room
import com.ioffeivan.alarmclock.core.data.source.local.db.AppRoomDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class StorageModule {

    @Singleton
    @Provides
    fun provideAppRoomDatabase(context: Context): AppRoomDatabase {
        return Room.databaseBuilder(
                context,
                AppRoomDatabase::class.java,
                "alarm_clocks_database"
            ).fallbackToDestructiveMigration(true).build()
    }
}
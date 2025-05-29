package com.ioffeivan.alarmclock.di

import android.content.Context
import androidx.room.Room
import com.ioffeivan.alarmclock.core.data.source.local.db.AppRoomDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Named

@Module
@InstallIn(SingletonComponent::class)
class TestStorageModule {

    @Provides
    @Named("test_db")
    fun provideInMemoryAppRoomDatabase(@ApplicationContext context: Context): AppRoomDatabase {
        return Room.inMemoryDatabaseBuilder(context = context, klass = AppRoomDatabase::class.java)
            .allowMainThreadQueries()
            .build()
    }
}
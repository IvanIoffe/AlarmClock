package com.ioffeivan.alarmclock.spotify.connection.data.di

import com.ioffeivan.alarmclock.spotify.connection.data.repository.SpotifyConnectionRepositoryImpl
import com.ioffeivan.alarmclock.spotify.connection.domain.repository.SpotifyConnectionRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
interface SpotifyConnectionModule {

    @Binds
    fun bindSpotifyConnectionRepositoryImpl(
        spotifyConnectionRepositoryImpl: SpotifyConnectionRepositoryImpl,
    ): SpotifyConnectionRepository
}
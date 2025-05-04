package com.ioffeivan.alarmclock.spotify.auth.data.source.remote.di

import com.ioffeivan.alarmclock.app.di.SpotifyAuth
import com.ioffeivan.alarmclock.spotify.auth.data.repository.SpotifyAuthRepositoryImpl
import com.ioffeivan.alarmclock.spotify.auth.data.source.remote.RetrofitSpotifyAuthRemoteDataSource
import com.ioffeivan.alarmclock.spotify.auth.data.source.remote.SpotifyAuthRemoteDataSource
import com.ioffeivan.alarmclock.spotify.auth.data.source.remote.service.SpotifyAuthService
import com.ioffeivan.alarmclock.spotify.auth.domain.repository.SpotifyAuthRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.create
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class SpotifyAuthModuleProvider {

    @Singleton
    @Provides
    fun provideSpotifyAuthService(@SpotifyAuth retrofitBuilder: Retrofit.Builder): SpotifyAuthService {
        return retrofitBuilder
            .build()
            .create()
    }
}

@InstallIn(SingletonComponent::class)
@Module
interface SpotifyAuthModuleBinder {

    @Binds
    fun bindSpotifyAuthRepositoryImpl(spotifyAuthRepositoryImpl: SpotifyAuthRepositoryImpl): SpotifyAuthRepository

    @Binds
    fun bindRetrofitSpotifyAuthRemoteDataSource(
        retrofitSpotifyAuthRemoteDataSource: RetrofitSpotifyAuthRemoteDataSource
    ): SpotifyAuthRemoteDataSource
}
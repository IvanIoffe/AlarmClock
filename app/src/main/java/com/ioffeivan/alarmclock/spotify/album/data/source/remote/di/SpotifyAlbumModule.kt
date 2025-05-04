package com.ioffeivan.alarmclock.spotify.album.data.source.remote.di

import com.ioffeivan.alarmclock.spotify.album.data.repository.AlbumRepositoryImpl
import com.ioffeivan.alarmclock.spotify.album.data.source.remote.AlbumRemoteDataSource
import com.ioffeivan.alarmclock.spotify.album.data.source.remote.RetrofitAlbumRemoteDataSource
import com.ioffeivan.alarmclock.spotify.album.data.source.remote.service.AlbumApiService
import com.ioffeivan.alarmclock.spotify.album.domain.repository.AlbumRepository
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
class SpotifyAlbumModuleProvider {

    @Singleton
    @Provides
    fun provideAlbumApiService(retrofitBuilder: Retrofit.Builder): AlbumApiService {
        return retrofitBuilder
            .build()
            .create()
    }
}

@InstallIn(SingletonComponent::class)
@Module
interface SpotifyAlbumModuleBinder {

    @Binds
    fun bindAlbumRepositoryImpl(albumRepositoryImpl: AlbumRepositoryImpl): AlbumRepository

    @Binds
    fun bindRetrofitAlbumRemoteDataSource(
        retrofitAlbumRemoteDataSource: RetrofitAlbumRemoteDataSource
    ): AlbumRemoteDataSource
}
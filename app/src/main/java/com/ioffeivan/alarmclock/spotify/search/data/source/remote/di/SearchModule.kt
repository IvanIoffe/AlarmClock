package com.ioffeivan.alarmclock.spotify.search.data.source.remote.di

import com.ioffeivan.alarmclock.spotify.search.data.repository.SearchRepositoryImpl
import com.ioffeivan.alarmclock.spotify.search.data.service.SearchApiService
import com.ioffeivan.alarmclock.spotify.search.data.source.remote.RetrofitSearchRemoteDataSource
import com.ioffeivan.alarmclock.spotify.search.data.source.remote.SearchRemoteDataSource
import com.ioffeivan.alarmclock.spotify.search.domain.repository.SearchRepository
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
class SearchModuleProvider {

    @Singleton
    @Provides
    fun provideSearchApiService(retrofitBuilder: Retrofit.Builder): SearchApiService {
        return retrofitBuilder
            .build()
            .create()
    }
}

@InstallIn(SingletonComponent::class)
@Module
interface SearchModuleBinder {

    @Binds
    fun bindSearchRepositoryImpl(searchRepositoryImpl: SearchRepositoryImpl): SearchRepository

    @Binds
    fun bindRetrofitSearchRemoteDataSource(
        retrofitSearchRemoteDataSource: RetrofitSearchRemoteDataSource
    ): SearchRemoteDataSource
}
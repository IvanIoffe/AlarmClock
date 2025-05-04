package com.ioffeivan.alarmclock.spotify.auth.data.repository

import com.ioffeivan.alarmclock.core.data.ApiResponse
import com.ioffeivan.alarmclock.core.data.mapper.mapToDomainModelFlow
import com.ioffeivan.alarmclock.spotify.auth.data.mapper.toAuthResponse
import com.ioffeivan.alarmclock.spotify.auth.data.source.remote.SpotifyAuthRemoteDataSource
import com.ioffeivan.alarmclock.spotify.auth.domain.model.AuthResponse
import com.ioffeivan.alarmclock.spotify.auth.domain.repository.SpotifyAuthRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SpotifyAuthRepositoryImpl @Inject constructor(
    private val spotifyAuthRemoteDataSource: SpotifyAuthRemoteDataSource
) : SpotifyAuthRepository {

    override fun auth(): Flow<ApiResponse<AuthResponse>> {
        return spotifyAuthRemoteDataSource.auth().mapToDomainModelFlow {
            it.toAuthResponse()
        }
    }
}
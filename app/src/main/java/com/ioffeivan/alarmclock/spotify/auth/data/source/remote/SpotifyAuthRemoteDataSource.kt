package com.ioffeivan.alarmclock.spotify.auth.data.source.remote

import com.ioffeivan.alarmclock.core.data.ApiResponse
import com.ioffeivan.alarmclock.spotify.auth.data.source.remote.model.AuthResponseRemote
import kotlinx.coroutines.flow.Flow

interface SpotifyAuthRemoteDataSource {
    fun auth(): Flow<ApiResponse<AuthResponseRemote>>
}
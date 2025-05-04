package com.ioffeivan.alarmclock.spotify.auth.domain.repository

import com.ioffeivan.alarmclock.core.data.ApiResponse
import com.ioffeivan.alarmclock.spotify.auth.domain.model.AuthResponse
import kotlinx.coroutines.flow.Flow

interface SpotifyAuthRepository {
    fun auth(): Flow<ApiResponse<AuthResponse>>
}
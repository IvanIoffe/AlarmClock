package com.ioffeivan.alarmclock.spotify.connection.domain.repository

import com.ioffeivan.alarmclock.spotify.connection.domain.ConnectionResult
import kotlinx.coroutines.flow.Flow

interface SpotifyConnectionRepository {
    fun connect(): Flow<ConnectionResult>
}
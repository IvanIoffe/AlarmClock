package com.ioffeivan.alarmclock.spotify.connection.domain.usecase

import com.ioffeivan.alarmclock.spotify.connection.domain.ConnectionResult
import com.ioffeivan.alarmclock.spotify.connection.domain.repository.SpotifyConnectionRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SpotifyConnectUseCase @Inject constructor(
    private val spotifyConnectionRepository: SpotifyConnectionRepository,
) {
    operator fun invoke(): Flow<ConnectionResult> = spotifyConnectionRepository.connect()
}
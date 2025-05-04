package com.ioffeivan.alarmclock.spotify.auth.domain.usecase

import com.ioffeivan.alarmclock.spotify.auth.domain.repository.SpotifyAuthRepository
import javax.inject.Inject

class SpotifyAuthUseCase @Inject constructor(
    private val spotifyAuthRepository: SpotifyAuthRepository
) {
    operator fun invoke() = spotifyAuthRepository.auth()
}
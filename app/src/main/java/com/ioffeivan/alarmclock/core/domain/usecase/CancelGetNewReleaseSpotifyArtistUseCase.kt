package com.ioffeivan.alarmclock.core.domain.usecase

import com.ioffeivan.alarmclock.background.scheduler.GetNewReleaseSpotifyArtistScheduler
import com.ioffeivan.alarmclock.core.domain.model.AlarmClock
import javax.inject.Inject

class CancelGetNewReleaseSpotifyArtistUseCase @Inject constructor(
    private val getNewReleaseSpotifyArtistScheduler: GetNewReleaseSpotifyArtistScheduler,
) {
    operator fun invoke(alarmClock: AlarmClock) {
        getNewReleaseSpotifyArtistScheduler.cancel(alarmClock)
    }
}
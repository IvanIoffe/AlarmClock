package com.ioffeivan.alarmclock.spotify.track.presentation

import com.ioffeivan.alarmclock.spotify.track.domain.model.Track

sealed class SpotifyTracksScreenUiState {
    data object Initial : SpotifyTracksScreenUiState()
    data object Loading : SpotifyTracksScreenUiState()
    data class Content(val tracks: List<Track>) : SpotifyTracksScreenUiState()
    data object EmptyContent : SpotifyTracksScreenUiState()
    data object Error : SpotifyTracksScreenUiState()
}
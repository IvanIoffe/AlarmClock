package com.ioffeivan.alarmclock.spotify.artist.presentaion

import com.ioffeivan.alarmclock.spotify.artist.domain.model.Artist

sealed class SpotifyArtistsScreenUiState {
    data object Initial : SpotifyArtistsScreenUiState()
    data object Loading : SpotifyArtistsScreenUiState()
    data class Content(val artists: List<Artist>) : SpotifyArtistsScreenUiState()
    data object Error : SpotifyArtistsScreenUiState()
}
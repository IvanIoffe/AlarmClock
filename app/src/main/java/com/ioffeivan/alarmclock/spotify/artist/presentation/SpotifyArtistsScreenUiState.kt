package com.ioffeivan.alarmclock.spotify.artist.presentation

import com.ioffeivan.alarmclock.spotify.artist.domain.model.Artist

sealed class SpotifyArtistsScreenUiState {
    data object Initial : SpotifyArtistsScreenUiState()
    data object Loading : SpotifyArtistsScreenUiState()
    data class Content(val artists: List<Artist>) : SpotifyArtistsScreenUiState()
    data object EmptyContent : SpotifyArtistsScreenUiState()
    data object Error : SpotifyArtistsScreenUiState()
}
package com.ioffeivan.alarmclock.spotify.connection.presentation

sealed class SpotifyConnectionUIState {
    data object Initial : SpotifyConnectionUIState()
    data object CouldNotFindSpotifyApp : SpotifyConnectionUIState()
    data object NotAuthorized : SpotifyConnectionUIState()
    data object LoadingError : SpotifyConnectionUIState()
    data object Loading : SpotifyConnectionUIState()
    data object Connected : SpotifyConnectionUIState()
    data class Authorized(val token: String) : SpotifyConnectionUIState()
}
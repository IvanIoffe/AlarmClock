package com.ioffeivan.alarmclock.spotify.connection.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ioffeivan.alarmclock.spotify.connection.domain.ConnectionResult
import com.ioffeivan.alarmclock.spotify.connection.domain.usecase.SpotifyConnectUseCase
import com.spotify.android.appremote.api.error.CouldNotFindSpotifyApp
import com.spotify.android.appremote.api.error.NotLoggedInException
import com.spotify.android.appremote.api.error.UserNotAuthorizedException
import com.spotify.sdk.android.auth.AuthorizationResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class SpotifyConnectionViewModel @Inject constructor(
    private val spotifyConnectUseCase: SpotifyConnectUseCase,
) : ViewModel() {

    private val _state =
        MutableStateFlow<SpotifyConnectionUIState>(SpotifyConnectionUIState.Initial)
    val state = _state
        .onStart { connect() }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(),
            SpotifyConnectionUIState.Initial
        )

    fun connect() {
        _state.value = SpotifyConnectionUIState.Loading
        spotifyConnectUseCase()
            .onEach {
                _state.value = when (it) {
                    is ConnectionResult.Connected -> {
                        SpotifyConnectionUIState.Connected
                    }

                    is ConnectionResult.Failure -> {
                        when (it.throwable) {
                            is CouldNotFindSpotifyApp -> {
                                SpotifyConnectionUIState.CouldNotFindSpotifyApp
                            }

                            is NotLoggedInException, is UserNotAuthorizedException -> {
                                SpotifyConnectionUIState.NotAuthorized
                            }

                            else -> {
                                SpotifyConnectionUIState.LoadingError
                            }
                        }
                    }
                }
            }
            .launchIn(viewModelScope)

    }

    fun handleAuthResponse(authResponse: AuthorizationResponse) {
        _state.value = when (authResponse.type) {
            AuthorizationResponse.Type.TOKEN -> {
                SpotifyConnectionUIState.Authorized(token = authResponse.accessToken)
            }

            else -> {
                SpotifyConnectionUIState.LoadingError
            }
        }
    }
}
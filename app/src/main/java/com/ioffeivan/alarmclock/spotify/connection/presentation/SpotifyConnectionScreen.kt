package com.ioffeivan.alarmclock.spotify.connection.presentation

import android.app.Activity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.ioffeivan.alarmclock.app.MainActivity
import com.ioffeivan.alarmclock.R
import com.ioffeivan.alarmclock.sound_selection.domain.model.SoundType
import com.ioffeivan.alarmclock.spotify.common.presentation.ErrorScreen
import com.ioffeivan.alarmclock.spotify.common.presentation.TokenViewModel
import com.ioffeivan.alarmclock.spotify.connection.presentation.utils.openSpotifyInApplicationStore
import com.ioffeivan.alarmclock.spotify.connection.SpotifyAuthenticator
import com.spotify.sdk.android.auth.AuthorizationClient

@Composable
fun SpotifyConnectionScreen(
    spotifyConnectionViewModel: SpotifyConnectionViewModel = hiltViewModel(),
    tokenViewModel: TokenViewModel,
    spotifyContentType: String,
    navigateToSpotifyTracksScreen: () -> Unit,
    navigateToSpotifyArtistsScreen: () -> Unit,
) {
    val uiState by spotifyConnectionViewModel.state.collectAsStateWithLifecycle()

    val activity = LocalContext.current as MainActivity
    val authLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        val intent = result.data
        if (result.resultCode == Activity.RESULT_OK && intent != null) {
            val response = AuthorizationClient.getResponse(result.resultCode, intent)
            spotifyConnectionViewModel.handleAuthResponse(authResponse = response)
        }
    }

    LaunchedEffect(uiState) {
        when (val state = uiState) {
            SpotifyConnectionUIState.Connected -> {
                SpotifyAuthenticator.auth(contextActivity = activity, launcher = authLauncher)
            }

            is SpotifyConnectionUIState.Authorized -> {
                tokenViewModel.setAccessToken(state.token)
                when (spotifyContentType) {
                    SoundType.SPOTIFY_TRACK.name -> {
                        navigateToSpotifyTracksScreen()
                    }

                    SoundType.NEW_RELEASE_SPOTIFY_ARTIST.name -> {
                        navigateToSpotifyArtistsScreen()
                    }

                    else -> {}
                }
            }

            else -> {}
        }
    }

    when (uiState) {
        SpotifyConnectionUIState.Loading, SpotifyConnectionUIState.Connected -> {
            SpotifyConnectionLoadingScreen()
        }

        SpotifyConnectionUIState.CouldNotFindSpotifyApp -> {
            ErrorScreen(
                errorMessage = stringResource(id = R.string.could_not_find_spotify_app_message),
                actionText = stringResource(id = R.string.could_not_find_spotify_app_action_text),
                onButtonClick = {
                    openSpotifyInApplicationStore(activity)
                }
            )
        }

        SpotifyConnectionUIState.NotAuthorized -> {
            ErrorScreen(
                errorMessage = stringResource(id = R.string.not_authorized_message),
                actionText = stringResource(id = R.string.not_authorized_action_text),
                onButtonClick = {
                    SpotifyAuthenticator.auth(contextActivity = activity, launcher = authLauncher)
                }
            )
        }

        SpotifyConnectionUIState.LoadingError -> {
            ErrorScreen(
                errorMessage = stringResource(id = R.string.error_loading_message),
                actionText = stringResource(id = R.string.error_loading_action_text),
                onButtonClick = {
                    spotifyConnectionViewModel.connect()
                }
            )
        }

        SpotifyConnectionUIState.Initial, is SpotifyConnectionUIState.Authorized -> {
            Box(modifier = Modifier.fillMaxSize())
        }
    }
}

@Composable
fun SpotifyConnectionLoadingScreen() {
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.lottie_spotify_loading))

    Column(
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        LottieAnimation(
            modifier = Modifier
                .size(100.dp),
            composition = composition,
            iterations = LottieConstants.IterateForever,
            speed = 1.25f,
            isPlaying = true,
        )

        Spacer(modifier = Modifier.height(2.dp))

        Text(text = stringResource(R.string.connection_to_spotify))
    }
}
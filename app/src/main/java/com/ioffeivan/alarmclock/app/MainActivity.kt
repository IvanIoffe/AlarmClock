package com.ioffeivan.alarmclock.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.ioffeivan.alarmclock.core.utils.showTimeUntilAlarmClockToast
import com.ioffeivan.alarmclock.create_and_update_alarmclock.presentation.CreateAndUpdateAlarmClockScreen
import com.ioffeivan.alarmclock.home.presentation.HomeScreen
import com.ioffeivan.alarmclock.app.navigation.AppNavGraph
import com.ioffeivan.alarmclock.app.navigation.Screen
import com.ioffeivan.alarmclock.app.navigation.rememberNavigationState
import com.ioffeivan.alarmclock.sound_selection.presentation.SoundSelectionScreen
import com.ioffeivan.alarmclock.spotify.artist.presentaion.SpotifyArtistsScreen
import com.ioffeivan.alarmclock.spotify.common.presentation.TokenViewModel
import com.ioffeivan.alarmclock.spotify.connection.presentation.SpotifyConnectionScreen
import com.ioffeivan.alarmclock.spotify.track.presentation.SpotifyTracksScreen
import com.ioffeivan.alarmclock.ui.theme.AlarmClockTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val tokenViewModel: TokenViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()

        setContent {
            AlarmClockTheme {
                Surface {
                    MainScreen(
                        tokenViewModel = tokenViewModel,
                    )
                }
            }
        }
    }
}

@Composable
private fun MainScreen(
    tokenViewModel: TokenViewModel,
) {
    val context = LocalContext.current
    val navigationState = rememberNavigationState()

    AppNavGraph(
        navController = navigationState.navController,
        homeScreen = {
            HomeScreen(
                onAlarmClockClick = { alarmClock ->
                    navigationState.navigateToCreateAndUpdateAlarmClockScreen(alarmClock)
                },
                onFloatingActionButtonClick = {
                    navigationState.navigateToCreateAndUpdateAlarmClockScreen()
                },
            )
        },

        createAndUpdateAlarmClockScreen = { alarmClockId, soundViewModel ->
            CreateAndUpdateAlarmClockScreen(
                alarmClockId = alarmClockId,
                soundViewModel = soundViewModel,
                onNavigationIconTopAppBarClick = {
                    navigationState.navController.navigateUp()
                },
                onActionIconTopAppBarClick = { alarmClock ->
                    showTimeUntilAlarmClockToast(
                        context = context,
                        alarmClockTime = alarmClock.time,
                    )
                    navigationState.navController.navigateUp()
                },
                onAlarmClockSoundClick = {
                    navigationState.navigateTo(Screen.SoundSelection.route)
                },
            )
        },

        soundSelectionScreen = { soundViewModel ->
            SoundSelectionScreen(
                soundViewModel = soundViewModel,
                onNavigationIconTopAppBarClick = {
                    navigationState.navController.navigateUp()
                },
                onSpotifyContentTypeClick = { spotifyContentType ->
                    navigationState.navigateToSpotifyConnectionScreen(spotifyContentType)
                },
            )
        },

        spotifyConnectScreen = { spotifyContentType ->
            SpotifyConnectionScreen(
                tokenViewModel = tokenViewModel,
                spotifyContentType = spotifyContentType,
                navigateToSpotifyTracksScreen = {
                    navigationState.navigateToPopUpTo(
                        routeTo = Screen.SpotifyTracks.route,
                        routePopUpTo = Screen.SpotifyConnection.route
                    )
                },
                navigateToSpotifyArtistsScreen = {
                    navigationState.navigateToPopUpTo(
                        routeTo = Screen.SpotifyArtists.route,
                        routePopUpTo = Screen.SpotifyConnection.route
                    )
                }
            )
        },

        spotifyTracksScreen = { soundViewModel ->
            SpotifyTracksScreen(
                tokenViewModel = tokenViewModel,
                soundViewModel = soundViewModel,
                onLeadingIconClick = {
                    navigationState.navController.navigateUp()
                }
            )
        },

        spotifyArtistsScreen = { soundViewModel ->
            SpotifyArtistsScreen(
                tokenViewModel = tokenViewModel,
                soundViewModel = soundViewModel,
                onLeadingIconClick = {
                    navigationState.navController.navigateUp()
                }
            )
        },
    )
}
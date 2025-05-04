package com.ioffeivan.alarmclock.app.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.ioffeivan.alarmclock.sound_selection.presentation.SoundViewModel

@Composable
fun AppNavGraph(
    navController: NavHostController,
    homeScreen: @Composable () -> Unit,
    createAndUpdateAlarmClockScreen: @Composable (String?, SoundViewModel) -> Unit,
    soundSelectionScreen: @Composable (SoundViewModel) -> Unit,
    spotifyConnectScreen: @Composable (String) -> Unit,
    spotifyTracksScreen: @Composable (SoundViewModel) -> Unit,
    spotifyArtistsScreen: @Composable (SoundViewModel) -> Unit,
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Home.route,
    ) {
        composable(route = Screen.Home.route) {
            homeScreen()
        }

        composable(
            route = Screen.CreateAndUpdateAlarmClock.route,
            arguments = listOf(
                navArgument(name = Screen.ALARM_CLOCK_ID_KEY) {
                    type = NavType.StringType
                    nullable = true
                }
            ),
        ) {
            val alarmClockId = it.arguments?.getString(Screen.ALARM_CLOCK_ID_KEY)
            val soundViewModel = hiltViewModel<SoundViewModel>()
            createAndUpdateAlarmClockScreen(alarmClockId, soundViewModel)
        }

        composable(route = Screen.SoundSelection.route) { backStackEntry ->
            val parentEntry = remember(backStackEntry) {
                navController.getBackStackEntry(Screen.CreateAndUpdateAlarmClock.route)
            }
            val soundViewModel = hiltViewModel<SoundViewModel>(parentEntry)
            soundSelectionScreen(soundViewModel)
        }

        composable(
            route = Screen.SPOTIFY_CONNECTION_ROUTE,
            arguments = listOf(
                navArgument(name = Screen.SPOTIFY_CONTENT_TYPE_KEY) {
                    type = NavType.StringType
                }
            )
        ) {
            val spotifyContentType = it.arguments?.getString(Screen.SPOTIFY_CONTENT_TYPE_KEY) ?: ""
            spotifyConnectScreen(spotifyContentType)
        }

        composable(route = Screen.SPOTIFY_TRACKS_ROUTE) { backStackEntry ->
            val parentEntry = remember(backStackEntry) {
                navController.getBackStackEntry(Screen.CreateAndUpdateAlarmClock.route)
            }
            val soundViewModel = hiltViewModel<SoundViewModel>(parentEntry)

            spotifyTracksScreen(soundViewModel)
        }

        composable(route = Screen.SPOTIFY_ARTISTS_ROUTE) { backStackEntry ->
            val parentEntry = remember(backStackEntry) {
                navController.getBackStackEntry(Screen.CreateAndUpdateAlarmClock.route)
            }
            val soundViewModel = hiltViewModel<SoundViewModel>(parentEntry)

            spotifyArtistsScreen(soundViewModel)
        }
    }
}
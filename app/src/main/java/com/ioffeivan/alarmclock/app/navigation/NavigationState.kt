package com.ioffeivan.alarmclock.app.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.ioffeivan.alarmclock.core.domain.model.AlarmClock

class NavigationState(
    val navController: NavHostController,
) {
    fun navigateTo(route: String) {
        navController.navigate(route)
    }

    fun navigateToPopUpTo(routeTo: String, routePopUpTo: String) {
        navController.navigate(routeTo) {
            popUpTo(routePopUpTo) {
                inclusive = true
            }
        }
    }

    fun navigateToCreateAndUpdateAlarmClockScreen(alarmClock: AlarmClock) {
        navController.navigate(
            Screen.CreateAndUpdateAlarmClock.getRouteWithArgumentsForUpdateAlarmClock(alarmClock)
        )
    }

    fun navigateToCreateAndUpdateAlarmClockScreen() {
        navController.navigate(
            Screen.CreateAndUpdateAlarmClock.getRouteWithArgumentsForCreateAlarmClock()
        )
    }

    fun navigateToSpotifyConnectionScreen(spotifyContentType: String) {
        navController.navigate(Screen.SpotifyConnection.getRouteWithArguments(spotifyContentType))
    }
}

@Composable
fun rememberNavigationState(
    navController: NavHostController = rememberNavController()
): NavigationState {
    return remember {
        NavigationState(navController)
    }
}

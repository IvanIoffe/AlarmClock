package com.ioffeivan.alarmclock.app.navigation

import com.ioffeivan.alarmclock.core.domain.model.AlarmClock

sealed class Screen(
    val route: String
) {
    data object Home : Screen(HOME_SCREEN_ROUTE)

    data object CreateAndUpdateAlarmClock : Screen(CREATE_AND_UPDATE_ALARM_CLOCK_SCREEN_ROUTE) {
        private const val ROUTE_FOR_ARGUMENTS = "create_and_update_alarm_clock_screen"

        fun getRouteWithArgumentsForUpdateAlarmClock(alarmClock: AlarmClock): String {
            return "$ROUTE_FOR_ARGUMENTS?$ALARM_CLOCK_ID_KEY=${alarmClock.id}"
        }

        fun getRouteWithArgumentsForCreateAlarmClock(): String {
            return ROUTE_FOR_ARGUMENTS
        }
    }

    data object SoundSelection : Screen(SOUND_SELECTION_ROUTE)

    data object SpotifyConnection : Screen(SPOTIFY_CONNECTION_ROUTE) {
        private const val ROUTE_FOR_ARGUMENTS = "spotify_connection"

        fun getRouteWithArguments(spotifyContentType: String): String {
            return "$ROUTE_FOR_ARGUMENTS/$spotifyContentType"
        }
    }

    data object SpotifyTracks : Screen(SPOTIFY_TRACKS_ROUTE)

    data object SpotifyArtists : Screen(SPOTIFY_ARTISTS_ROUTE)

    companion object {

        const val ALARM_CLOCK_ID_KEY = "alarm_clock_id"
        const val SPOTIFY_CONTENT_TYPE_KEY = "spotify_content_type"

        const val HOME_SCREEN_ROUTE = "home_screen"
        const val CREATE_AND_UPDATE_ALARM_CLOCK_SCREEN_ROUTE =
            "create_and_update_alarm_clock_screen?$ALARM_CLOCK_ID_KEY={$ALARM_CLOCK_ID_KEY}"
        const val SOUND_SELECTION_ROUTE = "sound_selection"
        const val SPOTIFY_CONNECTION_ROUTE = "spotify_connection/{$SPOTIFY_CONTENT_TYPE_KEY}"
        const val SPOTIFY_TRACKS_ROUTE = "spotify_tracks"
        const val SPOTIFY_ARTISTS_ROUTE = "spotify_artists"
    }
}

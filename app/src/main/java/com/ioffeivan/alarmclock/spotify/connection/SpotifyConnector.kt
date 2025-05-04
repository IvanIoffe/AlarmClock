package com.ioffeivan.alarmclock.spotify.connection

import android.content.Context
import com.ioffeivan.alarmclock.BuildConfig
import com.ioffeivan.alarmclock.core.utils.LoadingError
import com.ioffeivan.alarmclock.core.utils.isNetworkAvailable
import com.spotify.android.appremote.api.ConnectionParams
import com.spotify.android.appremote.api.Connector
import com.spotify.android.appremote.api.SpotifyAppRemote

object SpotifyConnector {

    var spotifyAppRemote: SpotifyAppRemote? = null
        private set

    fun connect(
        context: Context,
        onConnected: () -> Unit,
        onFailure: (Throwable) -> Unit,
    ) {
        if (!isNetworkAvailable(context)) {
            onFailure(LoadingError())
            return
        }

        val connectionParams = ConnectionParams.Builder(BuildConfig.SPOTIFY_CLIENT_ID)
            .setRedirectUri(BuildConfig.SPOTIFY_REDIRECT_URI)
            .showAuthView(true)
            .build()

        SpotifyAppRemote.connect(
            context,
            connectionParams,
            object : Connector.ConnectionListener {
                override fun onConnected(appRemote: SpotifyAppRemote) {
                    spotifyAppRemote = appRemote
                    onConnected()
                }

                override fun onFailure(throwable: Throwable) {
                    onFailure(throwable)
                }
            },
        )
    }

    fun disconnect() {
        spotifyAppRemote?.let {
            SpotifyAppRemote.disconnect(it)
        }
        spotifyAppRemote = null
    }
}
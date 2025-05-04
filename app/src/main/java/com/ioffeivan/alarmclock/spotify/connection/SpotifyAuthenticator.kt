package com.ioffeivan.alarmclock.spotify.connection

import android.app.Activity
import android.content.Intent
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.result.ActivityResult
import com.ioffeivan.alarmclock.BuildConfig
import com.spotify.sdk.android.auth.AuthorizationClient
import com.spotify.sdk.android.auth.AuthorizationRequest
import com.spotify.sdk.android.auth.AuthorizationResponse

class SpotifyAuthenticator {
    companion object {
        fun auth(
            contextActivity: Activity,
            launcher: ManagedActivityResultLauncher<Intent, ActivityResult>,
        ) {
            val request = AuthorizationRequest.Builder(
                BuildConfig.SPOTIFY_CLIENT_ID,
                AuthorizationResponse.Type.TOKEN,
                BuildConfig.SPOTIFY_REDIRECT_URI,
            )
                .setScopes(arrayOf("streaming", "app-remote-control"))
                .build()

            val authIntent = AuthorizationClient.createLoginActivityIntent(contextActivity, request)
            launcher.launch(authIntent)
        }
    }
}
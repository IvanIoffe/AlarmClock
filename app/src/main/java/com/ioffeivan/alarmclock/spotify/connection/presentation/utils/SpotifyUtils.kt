package com.ioffeivan.alarmclock.spotify.connection.presentation.utils

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri

fun openSpotifyInApplicationStore(context: Context) {
    val branchLink =
        Uri.encode("https://spotify.link/content_linking?~campaign=${context.packageName}")
    val spotifyPackageName = "com.spotify.music"
    val referrer = "_branch_link=$branchLink"

    val uri = try {
        Uri.parse("market://details")
            .buildUpon()
            .appendQueryParameter("id", spotifyPackageName)
            .appendQueryParameter("referrer", referrer)
            .build()
    } catch (ignored: ActivityNotFoundException) {
        Uri.parse("https://play.google.com/store/apps/details")
            .buildUpon()
            .appendQueryParameter("id", spotifyPackageName)
            .appendQueryParameter("referrer", referrer)
            .build()
    }

    val intent = Intent(Intent.ACTION_VIEW, uri).apply {
        flags = Intent.FLAG_ACTIVITY_NEW_TASK
    }

    context.startActivity(intent)
}
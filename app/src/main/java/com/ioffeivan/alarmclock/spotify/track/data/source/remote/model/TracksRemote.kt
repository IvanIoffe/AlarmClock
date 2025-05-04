package com.ioffeivan.alarmclock.spotify.track.data.source.remote.model

import com.google.gson.annotations.SerializedName

data class TracksRemote(

    @SerializedName("items")
    val items: List<TrackRemote>,
)

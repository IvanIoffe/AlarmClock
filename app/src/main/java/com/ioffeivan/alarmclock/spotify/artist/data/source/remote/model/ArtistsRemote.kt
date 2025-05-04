package com.ioffeivan.alarmclock.spotify.artist.data.source.remote.model

import com.google.gson.annotations.SerializedName
import com.ioffeivan.alarmclock.spotify.common.data.source.remote.model.ArtistRemote

data class ArtistsRemote(

    @SerializedName("items")
    val items: List<ArtistRemote>
)

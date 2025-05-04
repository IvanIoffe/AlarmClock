package com.ioffeivan.alarmclock.spotify.search.data.source.remote.model

import com.google.gson.annotations.SerializedName
import com.ioffeivan.alarmclock.spotify.artist.data.source.remote.model.ArtistsRemote
import com.ioffeivan.alarmclock.spotify.track.data.source.remote.model.TracksRemote

data class SearchResponseRemote(

    @SerializedName("tracks")
    val tracks: TracksRemote? = null,

    @SerializedName("artists")
    val artists: ArtistsRemote? = null,
)
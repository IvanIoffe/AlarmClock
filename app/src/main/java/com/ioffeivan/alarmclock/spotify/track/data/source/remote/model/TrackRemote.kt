package com.ioffeivan.alarmclock.spotify.track.data.source.remote.model

import com.google.gson.annotations.SerializedName
import com.ioffeivan.alarmclock.spotify.common.data.source.remote.model.AlbumRemote
import com.ioffeivan.alarmclock.spotify.common.data.source.remote.model.ArtistRemote

data class TrackRemote(

    @SerializedName("id")
    val id: String,

    @SerializedName("album")
    val album: AlbumRemote? = null,

    @SerializedName("artists")
    val artists: List<ArtistRemote>,

    @SerializedName("name")
    val name: String,

    @SerializedName("uri")
    val uri: String,
)

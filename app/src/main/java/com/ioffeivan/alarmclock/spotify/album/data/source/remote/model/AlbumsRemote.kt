package com.ioffeivan.alarmclock.spotify.album.data.source.remote.model

import com.google.gson.annotations.SerializedName
import com.ioffeivan.alarmclock.spotify.common.data.source.remote.model.AlbumRemote

data class AlbumsRemote(

    @SerializedName("items")
    val items: List<AlbumRemote>,
)
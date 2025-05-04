package com.ioffeivan.alarmclock.spotify.common.data.source.remote.model

import com.google.gson.annotations.SerializedName

data class ArtistRemote(

    @SerializedName("id")
    val id: String,

    @SerializedName("images")
    val images: List<ImageRemote>? = null,

    @SerializedName("name")
    val name: String,

    @SerializedName("uri")
    val uri: String,
)

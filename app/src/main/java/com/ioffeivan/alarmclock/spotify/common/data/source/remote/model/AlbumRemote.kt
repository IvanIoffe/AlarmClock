package com.ioffeivan.alarmclock.spotify.common.data.source.remote.model

import com.google.gson.annotations.SerializedName

data class AlbumRemote(

    @SerializedName("id")
    val id: String,

    @SerializedName("images")
    val images: List<ImageRemote>,

    @SerializedName("name")
    val name: String,

    @SerializedName("release_date")
    val releaseDate: String,

    @SerializedName("release_date_precision")
    val releaseDatePrecision: String,

    @SerializedName("uri")
    val uri: String,

    @SerializedName("artists")
    val artists: List<ArtistRemote>,
)

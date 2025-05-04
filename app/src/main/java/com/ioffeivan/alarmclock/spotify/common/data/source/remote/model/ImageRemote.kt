package com.ioffeivan.alarmclock.spotify.common.data.source.remote.model

import com.google.gson.annotations.SerializedName

data class ImageRemote(

    @SerializedName("url")
    val url: String,
)
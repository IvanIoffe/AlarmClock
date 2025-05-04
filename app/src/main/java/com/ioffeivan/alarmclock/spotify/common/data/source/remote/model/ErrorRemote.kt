package com.ioffeivan.alarmclock.spotify.common.data.source.remote.model

import com.google.gson.annotations.SerializedName

data class ErrorRemote(

    @SerializedName("error")
    val errorInfoRemote: ErrorInfoRemote,
)

data class ErrorInfoRemote(

    @SerializedName("status")
    val status: Int,

    @SerializedName("message")
    val message: String,
)
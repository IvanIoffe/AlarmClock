package com.ioffeivan.alarmclock.spotify.auth.data.source.remote.model

import com.google.gson.annotations.SerializedName

data class AuthResponseRemote(

    @SerializedName("access_token")
    val accessToken: String,

    @SerializedName("token_type")
    val tokenType: String
)

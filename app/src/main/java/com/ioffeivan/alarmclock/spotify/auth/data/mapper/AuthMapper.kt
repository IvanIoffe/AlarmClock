package com.ioffeivan.alarmclock.spotify.auth.data.mapper

import com.ioffeivan.alarmclock.spotify.auth.data.source.remote.model.AuthResponseRemote
import com.ioffeivan.alarmclock.spotify.auth.domain.model.AuthResponse

fun AuthResponseRemote.toAuthResponse() =
    AuthResponse(
        accessToken = accessToken,
        tokenType = tokenType
    )
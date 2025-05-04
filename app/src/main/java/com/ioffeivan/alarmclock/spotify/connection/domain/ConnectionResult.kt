package com.ioffeivan.alarmclock.spotify.connection.domain

sealed class ConnectionResult {
    data object Connected : ConnectionResult()
    data class Failure(val throwable: Throwable) : ConnectionResult()
}
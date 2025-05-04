package com.ioffeivan.alarmclock.spotify.album.domain.model

import java.time.LocalDate

data class Album(
    val id: String,
    val name: String,
    val imageUrl: String,
    val releaseDate: LocalDate,
    val uri: String,
    val artists: String,
)
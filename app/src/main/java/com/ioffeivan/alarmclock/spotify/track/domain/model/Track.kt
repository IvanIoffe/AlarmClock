package com.ioffeivan.alarmclock.spotify.track.domain.model

import com.ioffeivan.alarmclock.spotify.artist.domain.model.Artist

data class Track(
    val id: String,
    val imageUrl: String,
    val artists: List<Artist>,
    val displayArtists: String,
    val name: String,
    val uri: String
)

fun Track.getTrackNameWithArtistsName() = "$name - $displayArtists"
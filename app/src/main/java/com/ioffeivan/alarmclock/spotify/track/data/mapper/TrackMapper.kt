package com.ioffeivan.alarmclock.spotify.track.data.mapper

import com.ioffeivan.alarmclock.spotify.artist.data.mapper.toArtist
import com.ioffeivan.alarmclock.spotify.track.data.source.remote.model.TrackRemote
import com.ioffeivan.alarmclock.spotify.track.data.source.remote.model.TracksRemote
import com.ioffeivan.alarmclock.spotify.track.domain.model.Track
import com.ioffeivan.alarmclock.spotify.track.domain.model.Tracks

fun TracksRemote.toTracks() =
    Tracks(
        items = items.map { it.toTrack() }
    )

fun TrackRemote.toTrack() =
    Track(
        id = id,
        imageUrl = album?.images?.firstOrNull()?.url ?: "",
        artists = artists.map { it.toArtist() },
        displayArtists = artists.joinToString(separator = ", ", transform = { it.name }),
        name = name,
        uri = uri
    )
package com.ioffeivan.alarmclock.spotify.artist.data.mapper

import com.ioffeivan.alarmclock.spotify.common.data.source.remote.model.ArtistRemote
import com.ioffeivan.alarmclock.spotify.artist.data.source.remote.model.ArtistsRemote
import com.ioffeivan.alarmclock.spotify.artist.domain.model.Artist
import com.ioffeivan.alarmclock.spotify.artist.domain.model.Artists

fun ArtistsRemote.toArtists() =
    Artists(
        items = items.map { it.toArtist() }
    )

fun ArtistRemote.toArtist() =
    Artist(
        id = id,
        imageUrl = images?.firstOrNull()?.url,
        name = name,
        uri = uri
    )
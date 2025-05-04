package com.ioffeivan.alarmclock.spotify.search.data.mapper

import com.ioffeivan.alarmclock.spotify.artist.data.mapper.toArtists
import com.ioffeivan.alarmclock.spotify.search.data.source.remote.model.SearchResponseRemote
import com.ioffeivan.alarmclock.spotify.search.domain.model.SearchResponse
import com.ioffeivan.alarmclock.spotify.search.domain.model.SearchType
import com.ioffeivan.alarmclock.spotify.track.data.mapper.toTracks

fun SearchResponseRemote.toSearchResponse() =
    SearchResponse(
        tracks = tracks?.toTracks(),
        artists = artists?.toArtists(),
    )

fun SearchType.toRequestParams(): String {
    return when (this) {
        SearchType.TRACK -> "track"
        SearchType.ARTIST -> "artist"
    }
}
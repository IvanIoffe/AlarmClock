package com.ioffeivan.alarmclock.spotify.search.domain.model

import com.ioffeivan.alarmclock.spotify.artist.domain.model.Artists
import com.ioffeivan.alarmclock.spotify.track.domain.model.Tracks

data class SearchResponse(
    val tracks: Tracks?,
    val artists: Artists?,
)

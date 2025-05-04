package com.ioffeivan.alarmclock.spotify.search.domain.model

data class SearchRequest(
    val token: String,
    val query: String,
    val type: SearchType,
)

enum class SearchType {
    TRACK,
    ARTIST,
}
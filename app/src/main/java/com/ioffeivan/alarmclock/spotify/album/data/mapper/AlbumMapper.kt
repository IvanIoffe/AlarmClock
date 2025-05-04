package com.ioffeivan.alarmclock.spotify.album.data.mapper

import com.ioffeivan.alarmclock.spotify.album.data.source.remote.model.AlbumsRemote
import com.ioffeivan.alarmclock.spotify.album.domain.model.Album
import com.ioffeivan.alarmclock.spotify.album.domain.model.Albums
import com.ioffeivan.alarmclock.spotify.common.data.source.remote.model.AlbumRemote
import java.time.LocalDate
import java.time.Year
import java.time.YearMonth
import java.time.format.DateTimeFormatter

fun AlbumsRemote.toAlbums() =
    Albums(
        items = items.map { it.toAlbum() }
    )

fun AlbumRemote.toAlbum() =
    Album(
        id = id,
        name = name,
        imageUrl = images.firstOrNull()?.url ?: "",
        releaseDate = getReleaseDate(releaseDate = releaseDate, precision = releaseDatePrecision),
        uri = uri,
        artists = artists.joinToString(", ", transform = { it.name }),
    )

private fun getReleaseDate(releaseDate: String, precision: String): LocalDate {
    return when (precision) {
        "day" -> {
            LocalDate.parse(releaseDate, DateTimeFormatter.ofPattern("yyyy-MM-dd"))
        }

        "month" -> {
            YearMonth.parse(releaseDate, DateTimeFormatter.ofPattern("yyyy-MM"))
                .atDay(1)
        }

        "year" ->
            Year.parse(releaseDate, DateTimeFormatter.ofPattern("yyyy"))
                .atMonth(1)
                .atDay(1)

        else -> throw IllegalArgumentException("Неизвестная точность даты: $precision")
    }
}
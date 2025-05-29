package com.ioffeivan.alarmclock.spotify.track.presentation

import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import com.ioffeivan.alarmclock.core.data.ApiResponse
import com.ioffeivan.alarmclock.spotify.artist.domain.model.Artist
import com.ioffeivan.alarmclock.spotify.artist.domain.model.Artists
import com.ioffeivan.alarmclock.spotify.search.domain.model.SearchRequest
import com.ioffeivan.alarmclock.spotify.search.domain.model.SearchResponse
import com.ioffeivan.alarmclock.spotify.search.domain.model.SearchType
import com.ioffeivan.alarmclock.spotify.search.domain.usecase.SearchUseCase
import com.ioffeivan.alarmclock.spotify.track.domain.model.Track
import com.ioffeivan.alarmclock.spotify.track.domain.model.Tracks
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test

class SpotifyTracksScreenViewModelTest {

    private val searchUseCase = mockk<SearchUseCase>()
    private lateinit var spotifyTracksScreenViewModel: SpotifyTracksScreenViewModel

    @Before
    fun setUp() {
        spotifyTracksScreenViewModel = SpotifyTracksScreenViewModel(searchUseCase)
    }

    @After
    fun tearDown() {
        clearAllMocks()
    }

    @Test
    fun `state flow should emit initial value at start`() = runTest {
        spotifyTracksScreenViewModel.uiState.test {
            assertThat(SpotifyTracksScreenUiState.Initial).isEqualTo(awaitItem())
            cancel()
        }
    }

    @Test
    fun `state flow should emit Loading then Content when usecase emit Loading then Success(list(data))`() {
        runTest {
            val searchRequest = SearchRequest(
                token = "token",
                query = "query",
                type = SearchType.TRACK,
            )
            val trackList = createTrackList()
            val flowMock = flow {
                emit(ApiResponse.Loading)
                delay(10)
                emit(
                    ApiResponse.Success(
                        SearchResponse(
                            tracks = Tracks(items = trackList),
                            artists = Artists(items = listOf()),
                        )
                    )
                )
            }
            every { searchUseCase.invoke(searchRequest) } returns flowMock

            spotifyTracksScreenViewModel.searchTracks(searchRequest)

            spotifyTracksScreenViewModel.uiState.test {
                assertEquals(SpotifyTracksScreenUiState.Loading, awaitItem())
                assertEquals(SpotifyTracksScreenUiState.Content(trackList), awaitItem())
                cancel()
            }
        }
    }

    @Test
    fun `state flow should emit Loading then EmptyContent when usecase emit Loading then Success(list(empty))`() {
        runTest {
            val searchRequest = SearchRequest(
                token = "token",
                query = "query",
                type = SearchType.TRACK,
            )
            val flowMock = flow {
                emit(ApiResponse.Loading)
                delay(10)
                emit(
                    ApiResponse.Success(
                        SearchResponse(
                            tracks = Tracks(items = listOf()),
                            artists = Artists(items = listOf()),
                        )
                    )
                )
            }
            every { searchUseCase.invoke(searchRequest) } returns flowMock

            spotifyTracksScreenViewModel.searchTracks(searchRequest)

            spotifyTracksScreenViewModel.uiState.test {
                assertEquals(SpotifyTracksScreenUiState.Loading, awaitItem())
                assertEquals(SpotifyTracksScreenUiState.EmptyContent, awaitItem())
                cancel()
            }
        }
    }

    private fun createTrackList(): List<Track> {
        return listOf(
            Track(
                id = "",
                imageUrl = "",
                artists = emptyList<Artist>(),
                displayArtists = "",
                name = "",
                uri = "",
            ),
        )
    }
}
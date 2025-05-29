package com.ioffeivan.alarmclock.spotify.search.data.repository

import com.google.common.truth.Truth.assertThat
import com.ioffeivan.alarmclock.core.data.ApiResponse
import com.ioffeivan.alarmclock.spotify.artist.data.source.remote.model.ArtistsRemote
import com.ioffeivan.alarmclock.spotify.artist.domain.model.Artists
import com.ioffeivan.alarmclock.spotify.search.data.source.remote.SearchRemoteDataSource
import com.ioffeivan.alarmclock.spotify.search.data.source.remote.model.SearchResponseRemote
import com.ioffeivan.alarmclock.spotify.search.domain.model.SearchRequest
import com.ioffeivan.alarmclock.spotify.search.domain.model.SearchResponse
import com.ioffeivan.alarmclock.spotify.search.domain.model.SearchType
import com.ioffeivan.alarmclock.spotify.track.data.source.remote.model.TracksRemote
import com.ioffeivan.alarmclock.spotify.track.domain.model.Tracks
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test

class SearchRepositoryImplTest {

    private val searchRemoteDataSource = mockk<SearchRemoteDataSource>()
    private lateinit var searchRepositoryImpl: SearchRepositoryImpl

    @Before
    fun setUp() {
        searchRepositoryImpl = SearchRepositoryImpl(searchRemoteDataSource)
    }

    @After
    fun tearDown() {
        clearAllMocks()
    }

    @Test
    fun `should return mapped resource from remote source`() = runTest {
        val searchRequest = SearchRequest(
            token = "token",
            query = "query",
            type = SearchType.TRACK,
        )
        val remoteResponse = ApiResponse.Success(
            SearchResponseRemote(
                tracks = TracksRemote(items = listOf()),
                artists = ArtistsRemote(items = listOf()),
            )
        )
        val expectedResponse = ApiResponse.Success(
            SearchResponse(
                tracks = Tracks(items = listOf()),
                artists = Artists(items = listOf()),
            )
        )
        val flow = flowOf(remoteResponse)
        coEvery { searchRemoteDataSource.search(searchRequest) } returns flow

        val result = searchRepositoryImpl.search(searchRequest).first()

        assertThat(result is ApiResponse.Success).isTrue()
        assertThat(expectedResponse).isEqualTo(result)
    }

    @Test
    fun `should call search from remoteDataSource once`() = runTest {
        val searchRequest = SearchRequest(
            token = "token",
            query = "query",
            type = SearchType.TRACK,
        )
        val remoteResponse = ApiResponse.Success(
            SearchResponseRemote(
                tracks = TracksRemote(items = listOf()),
                artists = ArtistsRemote(items = listOf()),
            )
        )
        val flow = flowOf(remoteResponse)
        coEvery { searchRemoteDataSource.search(searchRequest) } returns flow

        searchRepositoryImpl.search(searchRequest)

        coVerify(exactly = 1) { searchRemoteDataSource.search(searchRequest) }
    }
}
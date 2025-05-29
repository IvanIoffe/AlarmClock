package com.ioffeivan.alarmclock.spotify.search.domain.usecase

import com.google.common.truth.Truth.assertThat
import com.ioffeivan.alarmclock.core.data.ApiResponse
import com.ioffeivan.alarmclock.spotify.artist.domain.model.Artists
import com.ioffeivan.alarmclock.spotify.search.domain.model.SearchRequest
import com.ioffeivan.alarmclock.spotify.search.domain.model.SearchResponse
import com.ioffeivan.alarmclock.spotify.search.domain.model.SearchType
import com.ioffeivan.alarmclock.spotify.search.domain.repository.SearchRepository
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

class SearchUseCaseTest {

    private val searchRepository = mockk<SearchRepository>()
    private lateinit var searchUseCase: SearchUseCase

    @Before
    fun setUp() {
        searchUseCase = SearchUseCase(searchRepository)
    }

    @After
    fun tearDown() {
        clearAllMocks()
    }

    @Test
    fun `should return flow from repository`() = runTest {
        val searchRequest = SearchRequest(
            token = "token",
            query = "query",
            type = SearchType.TRACK,
        )
        val expectedResponse = ApiResponse.Success(
            SearchResponse(
                tracks = Tracks(items = listOf()),
                artists = Artists(items = listOf()),
            )
        )
        val flow = flowOf(expectedResponse)
        coEvery { searchRepository.search(searchRequest) } returns flow

        val result = searchUseCase.invoke(searchRequest).first()

        assertThat(expectedResponse).isEqualTo(result)
    }

    @Test
    fun `should call search from repository once`() {
        val searchRequest = SearchRequest(
            token = "token",
            query = "query",
            type = SearchType.TRACK,
        )
        val expectedResponse = ApiResponse.Success(
            SearchResponse(
                tracks = Tracks(items = listOf()),
                artists = Artists(items = listOf()),
            )
        )
        val flow = flowOf(expectedResponse)
        coEvery { searchRepository.search(searchRequest) } returns flow

        searchUseCase(searchRequest)

        coVerify(exactly = 1) { searchRepository.search(searchRequest) }
    }
}
package com.ioffeivan.alarmclock.spotify.search.data.repository

import com.ioffeivan.alarmclock.core.data.ApiResponse
import com.ioffeivan.alarmclock.core.data.mapper.mapToDomainModelFlow
import com.ioffeivan.alarmclock.spotify.search.data.mapper.toSearchResponse
import com.ioffeivan.alarmclock.spotify.search.data.source.remote.SearchRemoteDataSource
import com.ioffeivan.alarmclock.spotify.search.domain.model.SearchRequest
import com.ioffeivan.alarmclock.spotify.search.domain.repository.SearchRepository
import com.ioffeivan.alarmclock.spotify.search.domain.model.SearchResponse
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SearchRepositoryImpl @Inject constructor(
    private val searchRemoteDataSource: SearchRemoteDataSource
) : SearchRepository {

    override fun search(searchRequest: SearchRequest): Flow<ApiResponse<SearchResponse>> {
        return searchRemoteDataSource.search(searchRequest).mapToDomainModelFlow(
            mapper = { searchResponseRemote ->
                searchResponseRemote.toSearchResponse()
            }
        )
    }
}
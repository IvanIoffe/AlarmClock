package com.ioffeivan.alarmclock.spotify.search.data.source.remote

import com.ioffeivan.alarmclock.core.data.ApiResponse
import com.ioffeivan.alarmclock.spotify.search.domain.model.SearchRequest
import com.ioffeivan.alarmclock.spotify.search.data.source.remote.model.SearchResponseRemote
import kotlinx.coroutines.flow.Flow

interface SearchRemoteDataSource {
    fun search(searchRequest: SearchRequest): Flow<ApiResponse<SearchResponseRemote>>
}
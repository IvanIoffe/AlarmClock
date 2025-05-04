package com.ioffeivan.alarmclock.spotify.search.domain.repository

import com.ioffeivan.alarmclock.core.data.ApiResponse
import com.ioffeivan.alarmclock.spotify.search.domain.model.SearchRequest
import com.ioffeivan.alarmclock.spotify.search.domain.model.SearchResponse
import kotlinx.coroutines.flow.Flow

interface SearchRepository {
    fun search(searchRequest: SearchRequest): Flow<ApiResponse<SearchResponse>>
}
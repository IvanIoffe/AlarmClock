package com.ioffeivan.alarmclock.spotify.search.data.source.remote

import com.ioffeivan.alarmclock.core.data.ApiResponse
import com.ioffeivan.alarmclock.core.data.apiRemoteRequestFlow
import com.ioffeivan.alarmclock.spotify.search.data.mapper.toRequestParams
import com.ioffeivan.alarmclock.spotify.search.data.source.remote.service.SearchApiService
import com.ioffeivan.alarmclock.spotify.search.data.source.remote.model.SearchResponseRemote
import com.ioffeivan.alarmclock.spotify.search.domain.model.SearchRequest
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class RetrofitSearchRemoteDataSource @Inject constructor(
    private val searchApiService: SearchApiService
) : SearchRemoteDataSource {

    override fun search(searchRequest: SearchRequest): Flow<ApiResponse<SearchResponseRemote>> {
        return apiRemoteRequestFlow {
            searchApiService.search(
                token = "Bearer ${searchRequest.token}",
                query = searchRequest.query,
                type = searchRequest.type.toRequestParams(),
            )
        }
    }
}
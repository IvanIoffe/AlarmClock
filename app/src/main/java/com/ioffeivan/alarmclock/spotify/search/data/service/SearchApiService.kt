package com.ioffeivan.alarmclock.spotify.search.data.service

import com.ioffeivan.alarmclock.spotify.search.data.source.remote.model.SearchResponseRemote
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface SearchApiService {

    @GET("v1/search/")
    suspend fun search(
        @Header("Authorization") token: String,
        @Query("query") query: String,
        @Query("type") type: String,
        @Query("limit") limit: Int = 50,
    ): Response<SearchResponseRemote>
}
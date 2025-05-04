package com.ioffeivan.alarmclock.core.data.mapper

import com.ioffeivan.alarmclock.core.data.ApiResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

fun <T, R> Flow<ApiResponse<T>>.mapToDomainModelFlow(mapper: (T) -> R): Flow<ApiResponse<R>> =
    map { response ->
        when (response) {
            is ApiResponse.Success -> {
                ApiResponse.Success(mapper(response.data))
            }

            is ApiResponse.Error -> ApiResponse.Error(response.message)
            ApiResponse.Loading -> ApiResponse.Loading
        }
    }

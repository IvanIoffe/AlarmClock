package com.ioffeivan.alarmclock.core.data

import com.google.gson.Gson
import com.ioffeivan.alarmclock.spotify.common.data.source.remote.model.ErrorRemote
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withTimeoutOrNull
import retrofit2.Response

fun <T> apiRemoteRequestFlow(call: suspend () -> Response<T>): Flow<ApiResponse<T>> = flow {
    emit(ApiResponse.Loading)

    withTimeoutOrNull(30000) {
        try {
            val response = call()

            if (response.isSuccessful) {
                response.body()?.let { data ->
                    emit(ApiResponse.Success(data))
                }
            } else {
                response.errorBody()?.let { error ->
                    val errorRemote = Gson().fromJson(error.charStream(), ErrorRemote::class.java)
                    emit(ApiResponse.Error(errorRemote.errorInfoRemote.message))
                }
            }
        } catch (e: Exception) {
            emit(ApiResponse.Error(e.message ?: e.toString()))
        }
    } ?: emit(ApiResponse.Error("Timeout! Please try again."))
}.flowOn(Dispatchers.IO)
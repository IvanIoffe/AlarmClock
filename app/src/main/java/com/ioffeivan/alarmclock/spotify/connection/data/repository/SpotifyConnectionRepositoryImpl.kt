package com.ioffeivan.alarmclock.spotify.connection.data.repository

import android.content.Context
import com.ioffeivan.alarmclock.spotify.connection.SpotifyConnector
import com.ioffeivan.alarmclock.spotify.connection.domain.ConnectionResult
import com.ioffeivan.alarmclock.spotify.connection.domain.repository.SpotifyConnectionRepository
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject

class SpotifyConnectionRepositoryImpl @Inject constructor(
    private val context: Context,
) : SpotifyConnectionRepository {

    override fun connect(): Flow<ConnectionResult> {
        return callbackFlow {
            SpotifyConnector.connect(
                context = context,
                onConnected = {
                    trySend(ConnectionResult.Connected)
                },
                onFailure = { throwable ->
                    trySend(ConnectionResult.Failure(throwable = throwable))
                },
            )

            awaitClose {}
        }
    }
}
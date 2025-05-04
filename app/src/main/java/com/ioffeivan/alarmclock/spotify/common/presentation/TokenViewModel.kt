package com.ioffeivan.alarmclock.spotify.common.presentation

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class TokenViewModel: ViewModel() {
    private val _accessToken = MutableStateFlow<String?>(null)
    val accessToken = _accessToken.asStateFlow()

    fun setAccessToken(newAccessToken: String) {
        _accessToken.value = newAccessToken
    }
}
package com.ioffeivan.alarmclock.audio_player

interface PlayerController {
    fun play(uri: String?)
    fun resume()
    fun pause()
    fun dispose()
}
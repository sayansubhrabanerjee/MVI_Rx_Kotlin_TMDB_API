package com.example.mvi_tmdb.mvi

sealed class DisplayInputEvent {
    sealed class ScreenLoad : DisplayInputEvent() {
        object Show : ScreenLoad()
    }
}

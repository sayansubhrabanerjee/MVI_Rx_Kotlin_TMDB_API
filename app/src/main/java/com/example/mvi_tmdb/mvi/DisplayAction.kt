package com.example.mvi_tmdb.mvi

sealed class DisplayAction {
    sealed class ScreenLoad : DisplayAction() {
        object Show : ScreenLoad()
    }
}

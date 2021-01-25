package com.example.mvi_tmdb.mvi

import com.example.mvi_tmdb.api.Movie

sealed class DisplayResult {
    object Terminate : DisplayResult()
    sealed class ScreenLoad : DisplayResult() {
        data class Show(val movies: List<Movie>) : ScreenLoad()
        //object Show : ScreenLoad()
        object Hide : ScreenLoad()
    }
}

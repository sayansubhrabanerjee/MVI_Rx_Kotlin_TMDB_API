package com.example.mvi_tmdb.repo

import com.example.mvi_tmdb.api.MoviesResponse
import io.reactivex.rxjava3.core.Flowable

interface IMoviesRepo {
    fun getPopularMovies(): Flowable<MoviesResponse>
}
package com.example.mvi_tmdb.repo

import com.example.mvi_tmdb.api.MoviesResponse
import io.reactivex.rxjava3.core.Single

interface IMoviesRepo {
    fun getPopularMovies(): Single<MoviesResponse>
}
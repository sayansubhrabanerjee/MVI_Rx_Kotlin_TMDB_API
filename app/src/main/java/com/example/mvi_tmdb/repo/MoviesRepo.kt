package com.example.mvi_tmdb.repo

import com.example.mvi_tmdb.api.MoviesResponse
import com.example.mvi_tmdb.api.MoviesService
import com.example.mvi_tmdb.api.getApiKey
import io.reactivex.rxjava3.core.Flowable

class MoviesRepo: IMoviesRepo {
    override fun getPopularMovies(): Flowable<MoviesResponse> {
        return MoviesService.buildRetrofit().getPopularMovies(
            apiKey = getApiKey(),
            page = 1
        )
    }
}
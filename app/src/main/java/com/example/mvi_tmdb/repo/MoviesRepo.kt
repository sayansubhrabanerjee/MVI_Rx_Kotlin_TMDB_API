package com.example.mvi_tmdb.repo

import com.example.mvi_tmdb.api.MoviesResponse
import com.example.mvi_tmdb.api.MoviesService
import com.example.mvi_tmdb.api.getApiKey
import io.reactivex.rxjava3.core.Single

class MoviesRepo: IMoviesRepo {
    override fun getPopularMovies(): Single<MoviesResponse> {
        return MoviesService.buildRetrofit().getPopularMovies(
            apiKey = getApiKey(),
            page = 1
        )
    }
}
package com.example.mvi_tmdb.api

import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class MoviesService {
    companion object MoviesServiceFactory {

        private const val BASE_URL = "https://api.themoviedb.org/3/"

        fun buildRetrofit(): IMoviesService =
            Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                .build()
                .create(IMoviesService::class.java)
    }
}
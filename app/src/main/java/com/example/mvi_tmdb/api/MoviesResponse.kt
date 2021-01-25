package com.example.mvi_tmdb.api

import com.google.gson.annotations.SerializedName

data class MoviesResponse(
    @SerializedName("page")
    val page: Int?,

    @SerializedName("results")
    val movies: List<Movie>?,

    @SerializedName("total_pages")
    val total_pages: Int?,

    @SerializedName("total_results")
    val total_results: Int?
)
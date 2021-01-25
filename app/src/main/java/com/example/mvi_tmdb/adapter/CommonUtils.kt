package com.example.mvi_tmdb.adapter

import android.content.Context
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.mvi_tmdb.R
import com.example.mvi_tmdb.api.Movie

class CommonUtils {

    companion object {

        fun checkRatings(voteAverage: Double): Int {
            return when (voteAverage) {
                in 7.0..10.0 -> R.drawable.ic_star_most_popular
                6.0 -> R.drawable.ic_star_average_popular
                else -> R.drawable.ic_star_least_popular
            }
        }

        fun configureGlide(imageView: ImageView, movie: Movie, context: Context) {
            val requestOptions = RequestOptions()
                .placeholder(R.drawable.ic_launcher_foreground)
                .error(R.drawable.ic_launcher_background)

            Glide
                .with(context)
                .applyDefaultRequestOptions(requestOptions)
                .load(MOVIES_POSTER_PATH_BASE_URL + movie.posterPath)
                .into(imageView)
        }

        private const val MOVIES_POSTER_PATH_BASE_URL = "https://image.tmdb.org/t/p/w92/"
    }
}
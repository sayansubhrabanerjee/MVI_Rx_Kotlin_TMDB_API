package com.example.mvi_tmdb.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.mvi_tmdb.R
import com.example.mvi_tmdb.api.Movie
import kotlinx.android.synthetic.main.movies_row.view.*

class MoviesAdapter(
    val context: Context,
    private val movies: List<Movie>,
    private val listener: Listener
) :
    RecyclerView.Adapter<MoviesAdapter.MoviesViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): MoviesViewHolder {
        val itemView = LayoutInflater.from(context).inflate(R.layout.movies_row, parent, false)
        return MoviesViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return movies.count()
    }

    override fun onBindViewHolder(moviesViewHolder: MoviesViewHolder, position: Int) {
        moviesViewHolder.bindData(movies[position], listener)
    }

    inner class MoviesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindData(movie: Movie?, listener: Listener) {
            movie?.let { movieData ->
                CommonUtils.configureGlide(itemView.imageView_avatar, movieData, context)
                itemView.textView_title.text = movieData.title
                itemView.textView_overview.text = movieData.overview
                itemView.imageView_popularity.setImageResource(CommonUtils.checkRatings(movieData.voteAverage!!))

                itemView.setOnClickListener { listener.onItemClick(movieData) }
            }
        }
    }

    interface Listener {
        fun onItemClick(movie: Movie)
    }
}
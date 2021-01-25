package com.example.mvi_tmdb

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mvi_tmdb.adapter.MoviesAdapter
import com.example.mvi_tmdb.api.Movie
import com.example.mvi_tmdb.api.MoviesService
import com.example.mvi_tmdb.api.getApiKey
import com.example.mvi_tmdb.mvi.DisplayInputEvent
import com.example.mvi_tmdb.mvi.DisplayScreen
import com.example.mvi_tmdb.mvi.DisplayViewModel
import com.example.mvi_tmdb.repo.MoviesRepo
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers
import io.reactivex.rxjava3.subjects.PublishSubject
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.movies_row.*

class MainActivity : AppCompatActivity(), DisplayScreen.UserInterface, MoviesAdapter.Listener {

    private val repo: MoviesRepo by lazy {
        MoviesRepo()
    }

    private val screen: DisplayScreen by lazy {
        DisplayScreen(repo)
    }

    private lateinit var moviesData: List<Movie>
    private lateinit var moviesAdapter: MoviesAdapter

    private val inputEvents = PublishSubject.create<DisplayInputEvent>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        screen.attach(this)
    }

    private fun setAdapter(movies: List<Movie>) {
        moviesData = ArrayList(movies)
        moviesAdapter = MoviesAdapter(this, moviesData, this)
        recyclerView_movies.adapter = moviesAdapter
    }

    override fun onResume() {
        super.onResume()
        inputEvents.onNext(DisplayInputEvent.ScreenLoad.Show)
    }

    override fun render(viewModel: DisplayViewModel) {
        when (viewModel) {
            is DisplayViewModel.ScreenLoad.Show -> {
                Log.i("mytest: ", "show movies")
                setAdapter(viewModel.movies)
            }
            DisplayViewModel.ScreenLoad.Hide -> {
                Log.i("mytest: ", "hide movies")
            }
        }
    }

    override fun inputEvents(): Observable<DisplayInputEvent> {
        return Observable.mergeArray(
            inputEvents
        )
    }

    override fun onItemClick(movie: Movie) {
        Toast.makeText(this, "Clicked: ${movie.title}", Toast.LENGTH_SHORT).show()
    }

    override fun onDestroy() {
        screen.terminate()
        screen.detach()
        super.onDestroy()

    }
}
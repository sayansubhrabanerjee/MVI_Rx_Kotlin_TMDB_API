package com.example.mvi_tmdb.mvi

import com.example.mvi_tmdb.repo.IMoviesRepo
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.ObservableTransformer
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import io.reactivex.rxjava3.subjects.PublishSubject
import io.reactivex.rxjava3.subjects.Subject

class DisplayScreen(
    private val repo: IMoviesRepo
) {
    private val disposables = CompositeDisposable()

    private val outputEvents: Observable<DisplayViewModel>
    private val inputEvents: Subject<DisplayInputEvent> = PublishSubject.create()
    private val terminalEvents: Subject<DisplayResult.Terminate> = PublishSubject.create()

    init {
        outputEvents =
            inputEvents.compose(inputEventsToActions())
                .publish {
                    Observable.mergeArray(
                        it.ofType(DisplayAction.ScreenLoad.Show::class.java).compose(
                            show(repo)
                        ),
                    )
                }
                .compose(
                    resultsToNewViewModel(
                        terminalEvents
                    )
                )
    }


    fun attach(ui: UserInterface) {
        disposables.addAll(
            outputEvents
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(ui::render),

            ui.inputEvents()
                .subscribe(inputEvents::onNext)
        )
    }

    fun detach() {
        disposables.clear()
    }

    fun terminate() {
        terminalEvents.onNext(DisplayResult.Terminate)
    }

    interface UserInterface {
        fun render(viewModel: DisplayViewModel)
        fun inputEvents(): Observable<DisplayInputEvent>
    }

    companion object {
        /*Input Event -> Action*/

        private fun inputEventsToActions() =
            ObservableTransformer<DisplayInputEvent, DisplayAction> { inputEvents ->
                inputEvents.publish {
                    Observable.mergeArray(
                        it.compose(showVersions()),
                    )
                }
            }

        private fun showVersions() =
            ObservableTransformer<DisplayInputEvent, DisplayAction> { events ->
                events.ofType(DisplayInputEvent.ScreenLoad.Show::class.java)
                    .map<DisplayAction> { DisplayAction.ScreenLoad.Show }
            }


        /*Action -> Result*/

        private fun show(repo: IMoviesRepo) =
            ObservableTransformer<DisplayAction.ScreenLoad.Show, DisplayResult.ScreenLoad> {
                it.flatMap {
                    repo.getPopularMovies().map { response ->
                        DisplayResult.ScreenLoad.Show(response.moviesList!!)
                    }.toObservable()
                }
            }

        private fun resultsToNewViewModel(terminalEvents: Subject<DisplayResult.Terminate>) =
            ObservableTransformer<DisplayResult, DisplayViewModel.ScreenLoad> { results ->
                val sharedResults = results
                    .mergeWith(terminalEvents)
                    .takeUntil { it is DisplayResult.Terminate }
                    .publish()
                    .autoConnect()

                val models = sharedResults
                    .ofType(DisplayResult.ScreenLoad::class.java)
                    .compose(resultsToNewUiState())

                Observable.mergeArray(models)
            }

        private fun resultsToNewUiState() =
            ObservableTransformer<DisplayResult.ScreenLoad, DisplayViewModel.ScreenLoad> {
                it.map { display ->
                    when(display) {
                        is DisplayResult.ScreenLoad.Show -> DisplayViewModel.ScreenLoad.Show(display.movies)
                        is DisplayResult.ScreenLoad.Hide -> DisplayViewModel.ScreenLoad.Hide
                    }
                }
            }
    }
}

package com.atmecode.androidmovie.presenter

import com.atmecode.androidmovie.contract.MovieListInteractor
import com.atmecode.androidmovie.contract.MovieListView
import com.atmecode.androidmovie.model.Movie

class MovieListPresenter(
    private val view: MovieListView,
    private val interactor: MovieListInteractor
) {

    fun loadMovies(genreId: Int) {
        view.showLoading()
        interactor.loadMovies(genreId, object : MovieListInteractor.LoadCallback {
            override fun onMoviesLoaded(movies: List<Movie>) {
                view.hideLoading()
                view.showMovies(movies)
            }

            override fun onError(message: String) {
                view.hideLoading()
                view.showError(message)
            }
        })
    }

    fun loadMoreMovies(genreId: Int) {
        interactor.loadMoreMovies(genreId, object : MovieListInteractor.LoadMoreCallback {
            override fun onMoreMoviesLoaded(movies: List<Movie>) {
                view.showMoreMovies(movies)
            }

            override fun onError(message: String) {
                view.showError(message)
            }
        })
    }

    fun onDestroy() {
        interactor.cancel()
    }
}

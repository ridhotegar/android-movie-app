package com.atmecode.androidmovie.contract

import com.atmecode.androidmovie.model.Movie

interface MovieListView {
    fun showLoading()
    fun hideLoading()
    fun showMovies(movies: List<Movie>)
    fun showMoreMovies(movies: List<Movie>)
    fun showError(message: String)
}

interface MovieListInteractor {

    fun loadMovies(genreId: Int, callback: LoadCallback)

    fun loadMoreMovies(genreId: Int, callback: LoadMoreCallback)

    fun cancel()

    interface LoadCallback {
        fun onMoviesLoaded(movies: List<Movie>)
        fun onError(message: String)
    }

    interface LoadMoreCallback {
        fun onMoreMoviesLoaded(movies: List<Movie>)
        fun onError(message: String)
    }
}

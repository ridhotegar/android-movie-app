package com.atmecode.androidmovie.contract

import com.atmecode.androidmovie.model.Genre

interface GenreView {
    fun showLoading()
    fun hideLoading()
    fun showGenres(genres: List<Genre>)
    fun showError(message: String)
}

interface GenreInteractor {

    fun getGenres(callback: Callback)

    fun cancel()

    interface Callback {
        fun onGenresLoaded(genres: List<Genre>)
        fun onError(message: String)
    }
}

package com.atmecode.androidmovie.presenter

import com.atmecode.androidmovie.contract.GenreInteractor
import com.atmecode.androidmovie.contract.GenreView
import com.atmecode.androidmovie.model.Genre

class GenrePresenter(
    private val view: GenreView,
    private val interactor: GenreInteractor
) {

    fun loadGenres() {
        view.showLoading()
        interactor.getGenres(object : GenreInteractor.Callback {
            override fun onGenresLoaded(genres: List<Genre>) {
                view.hideLoading()
                view.showGenres(genres)
            }

            override fun onError(message: String) {
                view.hideLoading()
                view.showError(message)
            }
        })
    }

    fun onDestroy() {
        interactor.cancel()
    }
}

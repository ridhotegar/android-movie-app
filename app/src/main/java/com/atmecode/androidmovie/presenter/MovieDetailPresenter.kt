package com.atmecode.androidmovie.presenter

import com.atmecode.androidmovie.contract.MovieDetailInteractor
import com.atmecode.androidmovie.contract.MovieDetailView
import com.atmecode.androidmovie.model.MovieDetail
import com.atmecode.androidmovie.model.Review

class MovieDetailPresenter(
    private val view: MovieDetailView,
    private val interactor: MovieDetailInteractor
) {

    fun loadMovieDetail(movieId: Int) {
        view.showLoading()
        interactor.loadMovieDetail(movieId, object : MovieDetailInteractor.DetailCallback {
            override fun onDetailLoaded(movie: MovieDetail) {
                view.hideLoading()
                view.showMovieDetail(movie)
            }

            override fun onError(message: String) {
                view.hideLoading()
                view.showError(message)
            }
        })
        loadReviews(movieId, 1)
        loadTrailer(movieId)
    }

    fun loadReviews(movieId: Int, page: Int) {
        interactor.loadReviews(movieId, page, object : MovieDetailInteractor.ReviewsCallback {
            override fun onReviewsLoaded(reviews: List<Review>, hasMore: Boolean) {
                if (page == 1) {
                    view.showReviews(reviews, hasMore)
                } else {
                    view.showMoreReviews(reviews, hasMore)
                }
            }

            override fun onError(message: String) {
                view.showReviewError(message)
            }
        })
    }

    fun loadMoreReviews(movieId: Int) {
        interactor.loadMoreReviews(movieId, object : MovieDetailInteractor.ReviewsCallback {
            override fun onReviewsLoaded(reviews: List<Review>, hasMore: Boolean) {
                view.showMoreReviews(reviews, hasMore)
            }

            override fun onError(message: String) {
                view.showReviewError(message)
            }
        })
    }

    private fun loadTrailer(movieId: Int) {
        interactor.loadTrailer(movieId, object : MovieDetailInteractor.TrailerCallback {
            override fun onTrailerLoaded(url: String) {
                view.showTrailerUrl(url)
            }

            override fun onTrailerNotFound() {
            }
        })
    }

    fun onDestroy() {
        interactor.cancel()
    }
}

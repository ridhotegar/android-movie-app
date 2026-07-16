package com.atmecode.androidmovie.contract

import com.atmecode.androidmovie.model.MovieDetail
import com.atmecode.androidmovie.model.Review

interface MovieDetailView {
    fun showLoading()
    fun hideLoading()
    fun showMovieDetail(movie: MovieDetail)
    fun showReviews(reviews: List<Review>, hasMore: Boolean)
    fun showMoreReviews(reviews: List<Review>, hasMore: Boolean)
    fun showTrailerUrl(url: String)
    fun showError(message: String)
    fun showReviewError(message: String)
}

interface MovieDetailInteractor {

    fun loadMovieDetail(movieId: Int, callback: DetailCallback)

    fun loadReviews(movieId: Int, page: Int, callback: ReviewsCallback)

    fun loadMoreReviews(movieId: Int, callback: ReviewsCallback)

    fun loadTrailer(movieId: Int, callback: TrailerCallback)

    fun cancel()

    interface DetailCallback {
        fun onDetailLoaded(movie: MovieDetail)
        fun onError(message: String)
    }

    interface ReviewsCallback {
        fun onReviewsLoaded(reviews: List<Review>, hasMore: Boolean)
        fun onError(message: String)
    }

    interface TrailerCallback {
        fun onTrailerLoaded(url: String)
        fun onTrailerNotFound()
    }
}

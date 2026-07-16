package com.atmecode.androidmovie.interactor

import com.atmecode.androidmovie.api.RetrofitClient
import com.atmecode.androidmovie.api.TmdbApiService
import com.atmecode.androidmovie.contract.MovieDetailInteractor
import com.atmecode.androidmovie.model.MovieDetail
import com.atmecode.androidmovie.model.ReviewResponse
import com.atmecode.androidmovie.model.VideoResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MovieDetailInteractor(
    private val api: TmdbApiService = RetrofitClient.instance
) : MovieDetailInteractor {

    private var detailCall: Call<*>? = null
    private var reviewsCall: Call<*>? = null
    private var videosCall: Call<*>? = null

    private var currentReviewPage = 1
    private var totalReviewPages = 1
    private var isLoadingReviews = false

    override fun loadMovieDetail(movieId: Int, callback: MovieDetailInteractor.DetailCallback) {
        detailCall?.cancel()
        val request = api.getMovieDetail(movieId)
        detailCall = request
        request.enqueue(object : Callback<MovieDetail> {
            override fun onResponse(call: Call<MovieDetail>, response: Response<MovieDetail>) {
                if (response.isSuccessful && response.body() != null) {
                    callback.onDetailLoaded(response.body()!!)
                } else {
                    callback.onError("Failed to load movie details: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<MovieDetail>, t: Throwable) {
                if (!call.isCanceled) {
                    callback.onError(t.message ?: "Failed to load movie details")
                }
            }
        })
    }

    override fun loadReviews(movieId: Int, page: Int, callback: MovieDetailInteractor.ReviewsCallback) {
        if (isLoadingReviews) return
        isLoadingReviews = true
        currentReviewPage = page
        val request = api.getMovieReviews(movieId, page)
        reviewsCall = request
        request.enqueue(object : Callback<ReviewResponse> {
            override fun onResponse(call: Call<ReviewResponse>, response: Response<ReviewResponse>) {
                if (response.isSuccessful && response.body() != null) {
                    totalReviewPages = response.body()!!.totalPages
                    isLoadingReviews = false
                    val hasMore = currentReviewPage < totalReviewPages
                    callback.onReviewsLoaded(response.body()!!.results, hasMore)
                } else {
                    isLoadingReviews = false
                    callback.onError("Failed to load reviews: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<ReviewResponse>, t: Throwable) {
                if (!call.isCanceled) {
                    isLoadingReviews = false
                    callback.onError(t.message ?: "Failed to load reviews")
                }
            }
        })
    }

    override fun loadMoreReviews(movieId: Int, callback: MovieDetailInteractor.ReviewsCallback) {
        if (isLoadingReviews || currentReviewPage >= totalReviewPages) return
        loadReviews(movieId, currentReviewPage + 1, callback)
    }

    override fun loadTrailer(movieId: Int, callback: MovieDetailInteractor.TrailerCallback) {
        val request = api.getMovieVideos(movieId)
        videosCall = request
        request.enqueue(object : Callback<VideoResponse> {
            override fun onResponse(call: Call<VideoResponse>, response: Response<VideoResponse>) {
                if (response.isSuccessful && response.body() != null) {
                    val trailer = response.body()!!.results.firstOrNull { video ->
                        video.site.equals("YouTube", ignoreCase = true) &&
                                video.type.equals("Trailer", ignoreCase = true)
                    }
                    if (trailer != null) {
                        callback.onTrailerLoaded("https://www.youtube.com/watch?v=${trailer.key}")
                    } else {
                        callback.onTrailerNotFound()
                    }
                } else {
                    callback.onTrailerNotFound()
                }
            }

            override fun onFailure(call: Call<VideoResponse>, t: Throwable) {
                callback.onTrailerNotFound()
            }
        })
    }

    override fun cancel() {
        detailCall?.cancel()
        reviewsCall?.cancel()
        videosCall?.cancel()
    }
}

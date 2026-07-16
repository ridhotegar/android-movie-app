package com.atmecode.androidmovie.interactor

import com.atmecode.androidmovie.api.RetrofitClient
import com.atmecode.androidmovie.api.TmdbApiService
import com.atmecode.androidmovie.contract.MovieListInteractor
import com.atmecode.androidmovie.model.MovieResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MovieListInteractor(
    private val api: TmdbApiService = RetrofitClient.instance
) : MovieListInteractor {

    private var call: Call<*>? = null

    private var currentPage = 1
    private var totalPages = 1
    private var isLoading = false

    override fun loadMovies(genreId: Int, callback: MovieListInteractor.LoadCallback) {
        if (isLoading) return
        call?.cancel()
        currentPage = 1
        isLoading = true
        val request = api.discoverMovies(genreId, currentPage)
        call = request
        request.enqueue(object : Callback<MovieResponse> {
            override fun onResponse(call: Call<MovieResponse>, response: Response<MovieResponse>) {
                if (response.isSuccessful && response.body() != null) {
                    totalPages = response.body()!!.totalPages
                    callback.onMoviesLoaded(response.body()!!.results)
                } else {
                    callback.onError("Failed to load movies: ${response.code()}")
                }
                isLoading = false
            }

            override fun onFailure(call: Call<MovieResponse>, t: Throwable) {
                if (!call.isCanceled) {
                    callback.onError(t.message ?: "Failed to load movies")
                }
                isLoading = false
            }
        })
    }

    override fun loadMoreMovies(genreId: Int, callback: MovieListInteractor.LoadMoreCallback) {
        if (isLoading || currentPage >= totalPages) return
        currentPage++
        isLoading = true
        val request = api.discoverMovies(genreId, currentPage)
        call = request
        request.enqueue(object : Callback<MovieResponse> {
            override fun onResponse(call: Call<MovieResponse>, response: Response<MovieResponse>) {
                if (response.isSuccessful && response.body() != null) {
                    callback.onMoreMoviesLoaded(response.body()!!.results)
                } else {
                    currentPage--
                    callback.onError("Failed to load more movies: ${response.code()}")
                }
                isLoading = false
            }

            override fun onFailure(call: Call<MovieResponse>, t: Throwable) {
                if (!call.isCanceled) {
                    currentPage--
                    callback.onError(t.message ?: "Failed to load more movies")
                }
                isLoading = false
            }
        })
    }

    override fun cancel() {
        call?.cancel()
    }
}

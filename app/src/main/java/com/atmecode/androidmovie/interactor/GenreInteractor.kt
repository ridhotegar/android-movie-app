package com.atmecode.androidmovie.interactor

import com.atmecode.androidmovie.api.RetrofitClient
import com.atmecode.androidmovie.api.TmdbApiService
import com.atmecode.androidmovie.contract.GenreInteractor
import com.atmecode.androidmovie.model.GenreResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class GenreInteractor(
    private val api: TmdbApiService = RetrofitClient.instance
) : GenreInteractor {

    private var call: Call<*>? = null

    override fun getGenres(callback: GenreInteractor.Callback) {
        call?.cancel()
        val request = api.getGenres()
        call = request
        request.enqueue(object : Callback<GenreResponse> {
            override fun onResponse(call: Call<GenreResponse>, response: Response<GenreResponse>) {
                if (response.isSuccessful && response.body() != null) {
                    callback.onGenresLoaded(response.body()!!.genres)
                } else {
                    callback.onError("Failed to load genres: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<GenreResponse>, t: Throwable) {
                if (!call.isCanceled) {
                    callback.onError(t.message ?: "Failed to load genres")
                }
            }
        })
    }

    override fun cancel() {
        call?.cancel()
    }
}

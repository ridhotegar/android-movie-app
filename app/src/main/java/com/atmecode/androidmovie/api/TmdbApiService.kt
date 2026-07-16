package com.atmecode.androidmovie.api

import com.atmecode.androidmovie.model.GenreResponse
import com.atmecode.androidmovie.model.MovieDetail
import com.atmecode.androidmovie.model.MovieResponse
import com.atmecode.androidmovie.model.ReviewResponse
import com.atmecode.androidmovie.model.VideoResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface TmdbApiService {

    @GET("genre/movie/list")
    fun getGenres(
        @Query("language") language: String = "en-US"
    ): Call<GenreResponse>

    @GET("discover/movie")
    fun discoverMovies(
        @Query("with_genres") genreId: Int,
        @Query("page") page: Int = 1,
        @Query("sort_by") sortBy: String = "popularity.desc",
        @Query("language") language: String = "en-US"
    ): Call<MovieResponse>

    @GET("movie/{movie_id}")
    fun getMovieDetail(
        @Path("movie_id") movieId: Int,
        @Query("language") language: String = "en-US"
    ): Call<MovieDetail>

    @GET("movie/{movie_id}/reviews")
    fun getMovieReviews(
        @Path("movie_id") movieId: Int,
        @Query("page") page: Int = 1,
        @Query("language") language: String = "en-US"
    ): Call<ReviewResponse>

    @GET("movie/{movie_id}/videos")
    fun getMovieVideos(
        @Path("movie_id") movieId: Int,
        @Query("language") language: String = "en-US"
    ): Call<VideoResponse>
}

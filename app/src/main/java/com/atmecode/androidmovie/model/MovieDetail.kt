package com.atmecode.androidmovie.model

import com.google.gson.annotations.SerializedName

data class MovieDetail(
    val id: Int,
    val title: String,
    @SerializedName("poster_path")
    val posterPath: String?,
    @SerializedName("backdrop_path")
    val backdropPath: String?,
    val overview: String,
    @SerializedName("vote_average")
    val voteAverage: Double,
    @SerializedName("release_date")
    val releaseDate: String,
    val runtime: Int?,
    val genres: List<Genre>?,
    @SerializedName("original_language")
    val originalLanguage: String,
    val popularity: Double,
    val tagline: String?,
    @SerializedName("vote_count")
    val voteCount: Int?
)

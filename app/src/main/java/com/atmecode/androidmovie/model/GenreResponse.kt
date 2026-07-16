package com.atmecode.androidmovie.model

import com.google.gson.annotations.SerializedName

data class Genre(
    val id: Int,
    val name: String
)

data class GenreResponse(
    val genres: List<Genre>
)

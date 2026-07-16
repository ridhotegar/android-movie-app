package com.atmecode.androidmovie.model

import com.google.gson.annotations.SerializedName

data class AuthorDetails(
    val username: String,
    @SerializedName("avatar_path")
    val avatarPath: String?,
    val rating: Double?
)

data class Review(
    val id: String,
    val author: String,
    val content: String,
    @SerializedName("created_at")
    val createdAt: String,
    @SerializedName("author_details")
    val authorDetails: AuthorDetails?
)

data class ReviewResponse(
    val page: Int,
    val results: List<Review>,
    @SerializedName("total_pages")
    val totalPages: Int,
    @SerializedName("total_results")
    val totalResults: Int
)

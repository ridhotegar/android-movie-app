package com.atmecode.androidmovie.util

import java.text.SimpleDateFormat
import java.util.Locale

object ReviewUiHelper {

    fun getStarRating(rating: Double): String {
        val fullStars = (rating / 2.0).toInt().coerceIn(0, 5)
        return buildString {
            repeat(fullStars) { append('\u2605') }
            repeat(5 - fullStars) { append('\u2606') }
        }
    }

    fun formatDate(isoDate: String?): String {
        if (isoDate.isNullOrEmpty()) return ""
        return try {
            val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.US)
            val outputFormat = SimpleDateFormat("MMM dd, yyyy", Locale.US)
            val date = inputFormat.parse(isoDate.take(19))
            date?.let { outputFormat.format(it) } ?: ""
        } catch (_: Exception) {
            try {
                val simpleFormat = SimpleDateFormat("yyyy-MM-dd", Locale.US)
                val outputFormat = SimpleDateFormat("MMM dd, yyyy", Locale.US)
                val date = simpleFormat.parse(isoDate.take(10))
                date?.let { outputFormat.format(it) } ?: ""
            } catch (_: Exception) {
                ""
            }
        }
    }

    fun getInitial(name: String): String {
        return name.trim().take(1).uppercase().ifEmpty { "?" }
    }

    fun getAvatarUrl(avatarPath: String?): String? {
        if (avatarPath == null) return null
        return if (avatarPath.startsWith("https://") || avatarPath.startsWith("http://")) {
            avatarPath
        } else {
            "https://image.tmdb.org/t/p/w185$avatarPath"
        }
    }
}

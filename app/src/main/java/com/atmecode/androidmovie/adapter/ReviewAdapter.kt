package com.atmecode.androidmovie.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.atmecode.androidmovie.databinding.ItemReviewBinding
import com.atmecode.androidmovie.model.Review
import com.atmecode.androidmovie.util.ReviewUiHelper
import com.bumptech.glide.Glide

class ReviewAdapter(
    private val onLoadMore: () -> Unit
) : RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder>() {

    private val reviews = mutableListOf<Review>()
    private val expandedStates = mutableSetOf<Int>()
    private var isLoadingMore = false

    fun setReviews(newReviews: List<Review>) {
        reviews.clear()
        reviews.addAll(newReviews)
        expandedStates.clear()
        isLoadingMore = false
        notifyDataSetChanged()
    }

    fun addReviews(moreReviews: List<Review>) {
        val startIndex = reviews.size
        reviews.addAll(moreReviews)
        isLoadingMore = false
        notifyItemRangeInserted(startIndex, moreReviews.size)
    }

    fun setLoadingMore(loading: Boolean) {
        isLoadingMore = loading
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReviewViewHolder {
        val binding = ItemReviewBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ReviewViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ReviewViewHolder, position: Int) {
        holder.bind(reviews[position], position)

        if (position >= itemCount - 2 && !isLoadingMore) {
            isLoadingMore = true
            onLoadMore()
        }
    }

    override fun getItemCount(): Int = reviews.size

    inner class ReviewViewHolder(private val binding: ItemReviewBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(review: Review, position: Int) {
            binding.textReviewAuthor.text = review.author

            val dateFormatted = ReviewUiHelper.formatDate(review.createdAt)
            if (dateFormatted.isNotEmpty()) {
                binding.textReviewDate.text = dateFormatted
                binding.textReviewDate.visibility = View.VISIBLE
            }

            val rating = review.authorDetails?.rating
            if (rating != null) {
                binding.textStarRating.text = ReviewUiHelper.getStarRating(rating)
                binding.textStarRating.visibility = View.VISIBLE

                val starColor = when {
                    rating >= 7.0 -> android.graphics.Color.parseColor("#4CAF50")
                    rating >= 4.0 -> android.graphics.Color.parseColor("#FF9800")
                    else -> android.graphics.Color.parseColor("#F44336")
                }
                binding.textStarRating.setTextColor(starColor)

                binding.textReviewRating.text = String.format("%.1f", rating)
                binding.textReviewRating.visibility = View.VISIBLE
            }

            val isExpanded = expandedStates.contains(position)
            binding.textReviewContent.maxLines = if (isExpanded) Int.MAX_VALUE else 4
            binding.textReviewContent.text = review.content

            binding.root.setOnClickListener {
                if (review.content.length > 200) {
                    val currentlyExpanded = expandedStates.contains(absoluteAdapterPosition)
                    if (currentlyExpanded) {
                        expandedStates.remove(absoluteAdapterPosition)
                        binding.textReviewContent.maxLines = 4
                        binding.textToggleExpand.text = "Read more"
                    } else {
                        expandedStates.add(absoluteAdapterPosition)
                        binding.textReviewContent.maxLines = Int.MAX_VALUE
                        binding.textToggleExpand.text = "Show less"
                    }
                }
            }

            val avatarPath = review.authorDetails?.avatarPath
            val avatarUrl = ReviewUiHelper.getAvatarUrl(avatarPath)

            if (avatarUrl != null) {
                binding.layoutAvatar.visibility = View.VISIBLE
                binding.textAvatarInitial.visibility = View.GONE
                binding.imageAvatar.visibility = View.VISIBLE
                Glide.with(binding.root.context)
                    .load(avatarUrl)
                    .placeholder(com.atmecode.androidmovie.R.drawable.bg_avatar_placeholder)
                    .error(com.atmecode.androidmovie.R.drawable.bg_avatar_placeholder)
                    .circleCrop()
                    .into(binding.imageAvatar)
            } else {
                binding.layoutAvatar.visibility = View.VISIBLE
                binding.imageAvatar.visibility = View.GONE
                binding.textAvatarInitial.visibility = View.VISIBLE
                binding.textAvatarInitial.text = ReviewUiHelper.getInitial(review.author)
            }

            if (review.content.length > 200) {
                binding.textToggleExpand.visibility = View.VISIBLE
                binding.textToggleExpand.text = if (isExpanded) "Show less" else "Read more"
            } else {
                binding.textToggleExpand.visibility = View.GONE
            }
        }
    }
}

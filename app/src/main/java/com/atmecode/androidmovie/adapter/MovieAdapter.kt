package com.atmecode.androidmovie.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.atmecode.androidmovie.R
import com.atmecode.androidmovie.api.RetrofitClient
import com.atmecode.androidmovie.databinding.ItemMovieBinding
import com.atmecode.androidmovie.model.Movie
import com.bumptech.glide.Glide

class MovieAdapter(
    private val onMovieClick: (Movie) -> Unit,
    private val onLoadMore: () -> Unit
) : RecyclerView.Adapter<MovieAdapter.MovieViewHolder>() {

    private val movies = mutableListOf<Movie>()
    private var isLoadingMore = false

    fun setMovies(newMovies: List<Movie>) {
        movies.clear()
        movies.addAll(newMovies)
        isLoadingMore = false
        notifyDataSetChanged()
    }

    fun addMovies(moreMovies: List<Movie>) {
        val startIndex = movies.size
        movies.addAll(moreMovies)
        isLoadingMore = false
        notifyItemRangeInserted(startIndex, moreMovies.size)
    }

    fun setLoadingMore(loading: Boolean) {
        isLoadingMore = loading
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val binding = ItemMovieBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return MovieViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        holder.bind(movies[position])

        if (position >= itemCount - 3 && !isLoadingMore) {
            isLoadingMore = true
            onLoadMore()
        }
    }

    override fun getItemCount(): Int = movies.size

    inner class MovieViewHolder(private val binding: ItemMovieBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(movie: Movie) {
            binding.textMovieTitle.text = movie.title
            binding.textMovieRating.text = "\u2605" + String.format(
                "%.1f", movie.voteAverage
            )
            binding.textMovieYear.text = if (movie.releaseDate.length >= 4) {
                movie.releaseDate.substring(0, 4)
            } else {
                movie.releaseDate
            }

            if (movie.posterPath != null) {
                Glide.with(binding.root.context)
                    .load(RetrofitClient.IMAGE_BASE_URL + movie.posterPath)
                    .placeholder(com.google.android.material.R.drawable.mtrl_ic_error)
                    .error(com.google.android.material.R.drawable.mtrl_ic_error)
                    .into(binding.imageMoviePoster)
            }

            binding.root.setOnClickListener { onMovieClick(movie) }
        }
    }
}

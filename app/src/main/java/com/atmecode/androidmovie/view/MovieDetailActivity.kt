package com.atmecode.androidmovie.view

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.atmecode.androidmovie.R
import com.atmecode.androidmovie.adapter.ReviewAdapter
import com.atmecode.androidmovie.api.RetrofitClient
import com.atmecode.androidmovie.databinding.ActivityMovieDetailBinding
import com.atmecode.androidmovie.interactor.MovieDetailInteractor
import com.atmecode.androidmovie.model.MovieDetail
import com.atmecode.androidmovie.model.Review
import com.atmecode.androidmovie.presenter.MovieDetailPresenter
import com.atmecode.androidmovie.contract.MovieDetailView
import com.bumptech.glide.Glide

class MovieDetailActivity : AppCompatActivity(), MovieDetailView {

    private lateinit var binding: ActivityMovieDetailBinding
    private lateinit var presenter: MovieDetailPresenter
    private lateinit var reviewAdapter: ReviewAdapter
    private var movieId: Int = 0
    private var trailerUrl: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMovieDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        movieId = intent.getIntExtra("movie_id", 0)
        val movieTitle = intent.getStringExtra("movie_title") ?: "Movie Details"

        binding.toolbar.title = movieTitle
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.toolbar.setNavigationOnClickListener { finish() }

        presenter = MovieDetailPresenter(this, MovieDetailInteractor())

        reviewAdapter = ReviewAdapter(
            onLoadMore = {
                presenter.loadMoreReviews(movieId)
            }
        )

        binding.recyclerReviews.layoutManager = LinearLayoutManager(this)
        binding.recyclerReviews.adapter = reviewAdapter

        binding.buttonTrailer.setOnClickListener {
            trailerUrl?.let { url ->
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                startActivity(intent)
            }
        }

        presenter.loadMovieDetail(movieId)
    }

    override fun showLoading() {
        binding.progressBar.visibility = View.VISIBLE
        binding.textError.visibility = View.GONE
    }

    override fun hideLoading() {
        binding.progressBar.visibility = View.GONE
    }

    override fun showMovieDetail(movie: MovieDetail) {
        binding.textTitle.text = movie.title

        if (!movie.tagline.isNullOrEmpty()) {
            binding.textTagline.text = movie.tagline
            binding.textTagline.visibility = View.VISIBLE
        }

        binding.textReleaseDate.text = getString(R.string.release_date_format, movie.releaseDate)
        binding.textRating.text = String.format("%.1f", movie.voteAverage)

        movie.voteCount?.let {
            binding.textVoteCount.text = getString(R.string.vote_count_format, it)
        }

        val runtime = movie.runtime
        if (runtime != null && runtime > 0) {
            val hours = runtime / 60
            val minutes = runtime % 60
            binding.textRuntime.text = getString(R.string.runtime_format, hours, minutes)
        } else {
            binding.textRuntime.visibility = View.GONE
        }

        movie.genres?.let { genreList ->
            if (genreList.isNotEmpty()) {
                val genreNames = genreList.joinToString(", ") { it.name }
                binding.textGenres.text = getString(R.string.genres_format, genreNames)
                binding.textGenres.visibility = View.VISIBLE
            }
        }

        binding.textOverview.text = movie.overview

        if (movie.posterPath != null) {
            Glide.with(this)
                .load(RetrofitClient.IMAGE_BASE_URL + movie.posterPath)
                .placeholder(com.google.android.material.R.drawable.mtrl_ic_error)
                .error(com.google.android.material.R.drawable.mtrl_ic_error)
                .into(binding.imagePoster)
        }
    }

    override fun showReviews(reviews: List<Review>, hasMore: Boolean) {
        if (reviews.isEmpty()) {
            binding.textNoReviews.visibility = View.VISIBLE
        } else {
            reviewAdapter.setReviews(reviews)
            reviewAdapter.setLoadingMore(!hasMore)
        }
    }

    override fun showMoreReviews(reviews: List<Review>, hasMore: Boolean) {
        reviewAdapter.addReviews(reviews)
        reviewAdapter.setLoadingMore(!hasMore)
    }

    override fun showTrailerUrl(url: String) {
        trailerUrl = url
        binding.buttonTrailer.visibility = View.VISIBLE
    }

    override fun showError(message: String) {
        binding.textError.visibility = View.VISIBLE
        binding.textError.text = message
    }

    override fun showReviewError(message: String) {
        binding.textReviewError.visibility = View.VISIBLE
        binding.textReviewError.text = message
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.onDestroy()
    }
}

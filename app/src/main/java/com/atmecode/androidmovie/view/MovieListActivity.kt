package com.atmecode.androidmovie.view

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.atmecode.androidmovie.adapter.MovieAdapter
import com.atmecode.androidmovie.databinding.ActivityMovieListBinding
import com.atmecode.androidmovie.interactor.MovieListInteractor
import com.atmecode.androidmovie.model.Movie
import com.atmecode.androidmovie.presenter.MovieListPresenter
import com.atmecode.androidmovie.contract.MovieListView

class MovieListActivity : AppCompatActivity(), MovieListView {

    private lateinit var binding: ActivityMovieListBinding
    private lateinit var presenter: MovieListPresenter
    private lateinit var adapter: MovieAdapter
    private var genreId: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMovieListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        genreId = intent.getIntExtra("genre_id", 0)
        val genreName = intent.getStringExtra("genre_name") ?: "Movies"

        binding.toolbar.title = genreName
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.toolbar.setNavigationOnClickListener { finish() }

        presenter = MovieListPresenter(this, MovieListInteractor())

        adapter = MovieAdapter(
            onMovieClick = { movie ->
                val intent = Intent(this, MovieDetailActivity::class.java).apply {
                    putExtra("movie_id", movie.id)
                    putExtra("movie_title", movie.title)
                }
                startActivity(intent)
            },
            onLoadMore = {
                presenter.loadMoreMovies(genreId)
            }
        )

        binding.recyclerMovies.layoutManager = GridLayoutManager(this, 2)
        binding.recyclerMovies.adapter = adapter

        presenter.loadMovies(genreId)
    }

    override fun showLoading() {
        binding.progressBar.visibility = View.VISIBLE
        binding.textError.visibility = View.GONE
    }

    override fun hideLoading() {
        binding.progressBar.visibility = View.GONE
    }

    override fun showMovies(movies: List<Movie>) {
        binding.recyclerMovies.visibility = View.VISIBLE
        if (movies.isEmpty()) {
            binding.textError.visibility = View.VISIBLE
            binding.textError.text = "No movies found for this genre"
        } else {
            adapter.setMovies(movies)
        }
    }

    override fun showMoreMovies(movies: List<Movie>) {
        adapter.addMovies(movies)
    }

    override fun showError(message: String) {
        binding.progressBar.visibility = View.GONE
        val currentCount = adapter.itemCount
        if (currentCount == 0) {
            binding.textError.visibility = View.VISIBLE
            binding.textError.text = message
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.onDestroy()
    }
}

package com.atmecode.androidmovie.view

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.atmecode.androidmovie.adapter.GenreAdapter
import com.atmecode.androidmovie.databinding.ActivityMainBinding
import com.atmecode.androidmovie.interactor.GenreInteractor
import com.atmecode.androidmovie.model.Genre
import com.atmecode.androidmovie.presenter.GenrePresenter
import com.atmecode.androidmovie.contract.GenreView

class MainActivity : AppCompatActivity(), GenreView {

    private lateinit var binding: ActivityMainBinding
    private lateinit var presenter: GenrePresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        presenter = GenrePresenter(this, GenreInteractor())
        presenter.loadGenres()
    }

    override fun showLoading() {
        binding.progressBar.visibility = View.VISIBLE
        binding.recyclerGenres.visibility = View.GONE
        binding.textError.visibility = View.GONE
    }

    override fun hideLoading() {
        binding.progressBar.visibility = View.GONE
    }

    override fun showGenres(genres: List<Genre>) {
        binding.recyclerGenres.visibility = View.VISIBLE
        binding.recyclerGenres.layoutManager = GridLayoutManager(this, 2)
        binding.recyclerGenres.adapter = GenreAdapter(genres) { genre ->
            val intent = Intent(this, MovieListActivity::class.java).apply {
                putExtra("genre_id", genre.id)
                putExtra("genre_name", genre.name)
            }
            startActivity(intent)
        }
    }

    override fun showError(message: String) {
        binding.textError.visibility = View.VISIBLE
        binding.textError.text = message
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.onDestroy()
    }
}

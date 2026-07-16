package com.atmecode.androidmovie.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.atmecode.androidmovie.databinding.ItemGenreBinding
import com.atmecode.androidmovie.model.Genre

class GenreAdapter(
    private val genres: List<Genre>,
    private val onGenreClick: (Genre) -> Unit
) : RecyclerView.Adapter<GenreAdapter.GenreViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GenreViewHolder {
        val binding = ItemGenreBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return GenreViewHolder(binding)
    }

    override fun onBindViewHolder(holder: GenreViewHolder, position: Int) {
        holder.bind(genres[position])
    }

    override fun getItemCount(): Int = genres.size

    inner class GenreViewHolder(private val binding: ItemGenreBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(genre: Genre) {
            binding.textGenreName.text = genre.name
            binding.root.setOnClickListener { onGenreClick(genre) }
        }
    }
}

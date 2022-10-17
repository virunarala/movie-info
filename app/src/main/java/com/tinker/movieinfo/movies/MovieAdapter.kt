package com.tinker.movieinfo.movies

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.tinker.movieinfo.databinding.ItemMovieBinding
import com.tinker.movieinfo.model.Movie

class MovieAdapter(val onClick: (Movie) -> Unit): ListAdapter<Movie, MovieAdapter.MovieViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        return MovieViewHolder(ItemMovieBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        Log.i(TAG,"Item sent to ViewHolder: ${getItem(position)}")
        holder.bind(getItem(position))
    }


    companion object DiffCallback: DiffUtil.ItemCallback<Movie>() {
        override fun areItemsTheSame(oldItem: Movie, newItem: Movie): Boolean {
            return oldItem.imdbId == newItem.imdbId
        }

        override fun areContentsTheSame(oldItem: Movie, newItem: Movie): Boolean {
            return oldItem == newItem
        }

        private const val TAG = "MovieAdapter"
    }

    inner class MovieViewHolder(private val binding: ItemMovieBinding): RecyclerView.ViewHolder(binding.root) {

        fun bind(movie: Movie) {
            Log.i(TAG,"Movie in Adapter: $movie")
            Glide
                .with(binding.imageMoviePoster.context)
                .load(movie.posterUrl)
                .into(binding.imageMoviePoster)

            binding.textMovieTitle.text = movie.title
            binding.textMovieYear.text = movie.year

            binding.viewMovie.setOnClickListener {
                Log.i(TAG,"onClick called")
                onClick(movie)
            }
        }
    }
}
package com.tinker.movieinfo.moviedetail

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import com.tinker.movieinfo.MainActivity
import com.tinker.movieinfo.R
import com.tinker.movieinfo.databinding.FragmentMovieDetailBinding
import com.tinker.movieinfo.model.Movie
import com.tinker.movieinfo.model.MovieDetailApiResponse
import com.tinker.movieinfo.model.NetworkResult
import com.tinker.movieinfo.movies.MoviesFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MovieDetailFragment : Fragment() {

    companion object {
        private const val TAG = "MovieDetailFrag"
    }

    private lateinit var binding: FragmentMovieDetailBinding
    private val viewModel: MovieDetailViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentMovieDetailBinding.inflate(inflater)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        hideBottomNav()
        collectMovieDetailFlow()

        val args = requireArguments()
        val imdbId = args.getString("imdb_id","")

        viewModel.getMovieDetails(imdbId)

        binding.buttonBack.setOnClickListener {
            requireActivity().onBackPressed()
        }
    }

    private fun hideBottomNav() {
        (requireActivity() as MainActivity).hideBottomNav()
    }

    private fun collectMovieDetailFlow() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.movieDetailFlow.collect { movieDetailFlow ->

                    when(movieDetailFlow) {
                        is NetworkResult.Loading -> {
                            binding.progressBarMovieDetail.visibility = View.VISIBLE
                        }
                        is NetworkResult.Success<*> -> {
                            //Received Employees data
                            binding.progressBarMovieDetail.visibility = View.GONE

                            val movie = movieDetailFlow.data as MovieDetailApiResponse

                            if(movie!=null) {
                                showMovie(movie)
                            } else {
                                Log.i(TAG,movie.toString())

                            }


                        }
                        is NetworkResult.Error -> {
                            //Error
                            binding.progressBarMovieDetail.visibility = View.GONE

                            val errorMessage = movieDetailFlow.message

                            //Display an error toast
                            Toast.makeText(context,errorMessage, Toast.LENGTH_SHORT).show()
                        }
                        is NetworkResult.NoInternet -> {
                            binding.progressBarMovieDetail.visibility = View.GONE
                            showNoInternetError()
                        }
                        else -> {
                            //Do nothing
                        }
                    }
                }
            }
        }
    }

    private fun showMovie(movie: MovieDetailApiResponse) {
        Glide
            .with(requireContext())
            .load(movie.poster)
            .into(binding.imagePoster)

        binding.textTitle.text = movie.title
        binding.textRating.text = movie.imdbRating
        binding.textDuration.text = movie.runtime
        binding.textPlot.text = movie.plot
        binding.textDirector.text = getString(R.string.string_director,movie.director)
        binding.textWriter.text = getString(R.string.string_writer,movie.writer)
        binding.textActors.text = getString(R.string.string_actors,movie.actors)
    }

    private fun showNoInternetError() {
        //Passing a random view from this layout as parameter.
        Snackbar.make(binding.progressBarMovieDetail, R.string.error_no_internet, Snackbar.LENGTH_SHORT)
            .setAction(R.string.string_dismiss) {

            }
            .show()
    }


}
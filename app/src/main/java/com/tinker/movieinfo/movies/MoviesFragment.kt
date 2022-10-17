package com.tinker.movieinfo.movies

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.SearchView
import android.widget.Toast
import androidx.fragment.app.commit
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.tinker.movieinfo.MainActivity
import com.tinker.movieinfo.R
import com.tinker.movieinfo.databinding.FragmentMoviesBinding
import com.tinker.movieinfo.model.Movie
import com.tinker.movieinfo.model.NetworkResult
import com.tinker.movieinfo.moviedetail.MovieDetailFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MoviesFragment : Fragment() {

    companion object {
        private const val TAG = "MoviesFragment"
    }

    private lateinit var binding: FragmentMoviesBinding

    private val viewModel: MoviesViewModel by viewModels()
    private lateinit var adapter: MovieAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentMoviesBinding.inflate(inflater)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        showBottomNav()
        adapter = MovieAdapter() {
            onMovieClick(it)
        }

        binding.recyclerViewMovies.adapter = adapter
        (binding.recyclerViewMovies.layoutManager as GridLayoutManager).spanCount = 3

        collectMoviesFlow()
        enableSearch()
//        viewModel.getMovies("avengers")
    }

    private fun enableSearch() {
        binding.searchMovies.setOnQueryTextListener(object: SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                search(query)
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })
    }

    private fun search(query: String?) {
        query?.let { viewModel.getMovies(it) }

    }

    private fun collectMoviesFlow() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.moviesFlow.collect { moviesFlow ->

                    when(moviesFlow) {
                        is NetworkResult.Loading -> {
                            binding.progressBarMovies.visibility = View.VISIBLE
                        }
                        is NetworkResult.Success<*> -> {
                            //Received Employees data
                            binding.progressBarMovies.visibility = View.GONE

                            val movies = moviesFlow.data as List<Movie>

                            if(movies.isEmpty()) {
                                enableErrorViews()
                            } else {
                                Log.i(TAG,movies.toString())
                                showMovies(movies)
                            }


                        }
                        is NetworkResult.Error -> {
                            //Error
                            binding.progressBarMovies.visibility = View.GONE

                            val errorMessage = moviesFlow.message

                            //Display an error toast
                            Toast.makeText(context,errorMessage, Toast.LENGTH_SHORT).show()
                        }
                        is NetworkResult.NoInternet -> {
                            binding.progressBarMovies.visibility = View.GONE
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

    private fun showBottomNav() {
        (requireActivity() as MainActivity).showBottomNav()
    }

    private fun showMovies(movies: List<Movie>) {
        Log.i(TAG,"Show movies called")
        adapter.submitList(movies)
        adapter.notifyDataSetChanged()
    }

    private fun onMovieClick(movie: Movie) {
        viewModel.addToHistory(movie)
        navigateToMovieDetail(movie)
    }

    private fun navigateToMovieDetail(movie: Movie) {

        val args = Bundle()
        args.putString("imdb_id",movie.imdbId)

        val movieDetailFragment = MovieDetailFragment()
        movieDetailFragment.arguments = args

        requireActivity().supportFragmentManager.commit {
            replace(R.id.host_fragment,movieDetailFragment)
            addToBackStack(null)
        }
    }

    private fun enableErrorViews() {
        binding.recyclerViewMovies.visibility = View.GONE

    }

    private fun showNoInternetError() {
        //Passing a random view from this layout as parameter.
        Snackbar.make(binding.progressBarMovies,R.string.error_no_internet, Snackbar.LENGTH_SHORT)
            .setAction(R.string.string_try_again) {

                //Retry Network call
                viewModel.getMovies("abcd")

            }
            .show()
    }


}
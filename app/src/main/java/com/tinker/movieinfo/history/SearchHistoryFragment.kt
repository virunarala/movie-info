package com.tinker.movieinfo.history

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.commit
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.tinker.movieinfo.MainActivity
import com.tinker.movieinfo.R
import com.tinker.movieinfo.databinding.FragmentSearchHistoryBinding
import com.tinker.movieinfo.model.Movie
import com.tinker.movieinfo.moviedetail.MovieDetailFragment
import com.tinker.movieinfo.movies.MovieAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SearchHistoryFragment : Fragment() {

    companion object {
        private const val TAG = "SearchHistoryFrag"
    }

    private lateinit var binding: FragmentSearchHistoryBinding
    private val viewModel: SearchHistoryViewModel by viewModels()
    private lateinit var adapter: MovieAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentSearchHistoryBinding.inflate(inflater)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        showBottomNav()
        adapter = MovieAdapter() {
            navigateToMovieDetail(it)
        }
        binding.recyclerViewSearchHistory.adapter = adapter
        (binding.recyclerViewSearchHistory.layoutManager as GridLayoutManager).spanCount = 3

        collectMoviesHistoryFlow()
    }

    override fun onResume() {
        viewModel.getSearchHistory()
        super.onResume()
    }

    private fun showBottomNav() {
        (requireActivity() as MainActivity).showBottomNav()
    }

    private fun collectMoviesHistoryFlow() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.searchHistoryFlow?.collect { movieEntities ->
                Log.i(TAG,"Collect called")
                val movies = movieEntities.map { Movie(it.title,it.year,it.imdbId,it.type,it.posterUrl) }
                adapter.submitList(movies)
                Log.i(TAG,"Movies: $movies")
                adapter.notifyDataSetChanged()
            }
        }
    }

    private fun navigateToMovieDetail(movie: Movie) {
        Log.i(TAG,"Navigate clicked")
        val args = Bundle()
        args.putString("imdb_id",movie.imdbId)

        val movieDetailFragment = MovieDetailFragment()
        movieDetailFragment.arguments = args

        requireActivity().supportFragmentManager.commit {
            replace(R.id.host_fragment,movieDetailFragment)
            addToBackStack(null)
        }
    }

}
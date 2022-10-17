package com.tinker.movieinfo.movies

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tinker.movieinfo.constants.NetworkResponseStatus
import com.tinker.movieinfo.constants.NetworkResponseStatus.SUCCESS
import com.tinker.movieinfo.database.MovieDao
import com.tinker.movieinfo.database.MovieEntity
import com.tinker.movieinfo.model.Movie
import com.tinker.movieinfo.model.NetworkResult
import com.tinker.movieinfo.network.ApiEndpoint
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.net.UnknownHostException
import javax.inject.Inject

@HiltViewModel
class MoviesViewModel @Inject constructor(private val apiEndpoint: ApiEndpoint,private val movieDao: MovieDao) : ViewModel() {

    companion object {
        private const val TAG = "MoviesViewModel"
    }

    private val _moviesFlow = MutableStateFlow<NetworkResult?>(null)
    val moviesFlow: StateFlow<NetworkResult?>
        get() = _moviesFlow

    fun getMovies(searchTerm: String) {
        _moviesFlow.value = NetworkResult.Loading
        viewModelScope.launch {
            _moviesFlow.value = getMoviesFromNetwork(searchTerm)
        }
    }

    private suspend fun getMoviesFromNetwork(searchTerm: String): NetworkResult {
        try {
            val response = apiEndpoint.getMovies(searchTerm)

            if(response.isSuccessful) {
                Log.i(TAG,"Success response received")
                //Received a response code in between 200-300
                val responseBody = response.body()
                if(responseBody?.response.contentEquals(SUCCESS)) {
                    Log.i(TAG,"Success true received")
                    Log.i(TAG,"Data from API: ${responseBody?.searchResult}")
                    //Received Success true from server
                    return NetworkResult.Success(responseBody?.searchResult as List<Movie>)
                } else {
                    //Received Success false from server
                    val errorMessage = responseBody?.errorMessage
                    if(errorMessage!=null) {
                        Log.i(TAG,"Error: $errorMessage")
                        return NetworkResult.Error("Error: $errorMessage")
                    }
                }
            } else {
                //Received Error response code from server
                return NetworkResult.Error("Server Error: ${response.message()}")
            }
            //Never reached
            return NetworkResult.Loading
        } catch (e: UnknownHostException) {
            Log.i(TAG,"Error: ${e.message}")
            return NetworkResult.NoInternet
        } catch (e: Exception) {
            Log.i(TAG,"Error: ${e.message}")
            return NetworkResult.Error("Error: ${e.message}")
        }

    }

    fun addToHistory(movie: Movie) {
        val movieEntity = MovieEntity(movie.imdbId,movie.title,movie.year,movie.type,movie.posterUrl)
        viewModelScope.launch {
            insertToSearchHistoryDb(movieEntity)
        }
    }

    private suspend fun insertToSearchHistoryDb(movie: MovieEntity) {
        movieDao.insertMovie(movie)
    }
}
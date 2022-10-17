package com.tinker.movieinfo.moviedetail

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tinker.movieinfo.constants.NetworkResponseStatus
import com.tinker.movieinfo.model.MovieDetailApiResponse
import com.tinker.movieinfo.model.NetworkResult
import com.tinker.movieinfo.network.ApiEndpoint
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.net.UnknownHostException
import javax.inject.Inject

@HiltViewModel
class MovieDetailViewModel @Inject constructor(private val apiEndpoint: ApiEndpoint) : ViewModel() {

    companion object {
        private const val TAG = "MovieDetailVM"
    }

    private val _movieDetailFlow = MutableStateFlow<NetworkResult?>(null)
    val movieDetailFlow: StateFlow<NetworkResult?>
        get() = _movieDetailFlow

    fun getMovieDetails(imdbId: String) {
        _movieDetailFlow.value = NetworkResult.Loading
        viewModelScope.launch {
            _movieDetailFlow.value = getMovieDetailsFromNetwork(imdbId)
        }
    }

    private suspend fun getMovieDetailsFromNetwork(imdbId: String): NetworkResult {
        try {
            val response = apiEndpoint.getMovieDetails(imdbId)

            if(response.isSuccessful) {
                Log.i(TAG,"Success response received")
                //Received a response code in between 200-300
                val responseBody = response.body()
                if(responseBody?.response.contentEquals(NetworkResponseStatus.SUCCESS)) {
                    Log.i(TAG,"Success true received")
                    Log.i(TAG,"Data from API: $responseBody")
                    //Received Success true from server
                    return NetworkResult.Success(responseBody as MovieDetailApiResponse)
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
}
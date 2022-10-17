package com.tinker.movieinfo.history

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tinker.movieinfo.database.MovieDao
import com.tinker.movieinfo.database.MovieEntity
import com.tinker.movieinfo.model.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchHistoryViewModel @Inject constructor(private val movieDao: MovieDao) : ViewModel() {

    companion object {
        private const val TAG = "SearchHistoryVM"
    }


    private val _searchHistoryFlow = MutableStateFlow<List<MovieEntity>>(listOf())
    val searchHistoryFlow: StateFlow<List<MovieEntity>>
        get() = _searchHistoryFlow

    fun getSearchHistory() {
        Log.i(TAG,"Search History called")
        viewModelScope.launch {
            _searchHistoryFlow.value = movieDao.getAllMovies()
        }
    }


}
package com.tinker.movieinfo.model

/**
 * A generic class that holds a value with its loading status.
 */
sealed class NetworkResult {

    data class Success<out T>(val data: T): NetworkResult()
    data class Error(val message: String): NetworkResult()
    object Loading: NetworkResult()
    object NoInternet: NetworkResult()

    override fun toString(): String {
        return when(this) {
            is Success<*> -> "Success[data=$data]"
            is Error -> "Error[message=$message]"
            Loading -> "Loading"
            NoInternet -> "No Internet"
        }
    }
}

val NetworkResult.isSuccess
    get() = this is NetworkResult.Success<*> && data!=null
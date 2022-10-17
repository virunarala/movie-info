package com.tinker.movieinfo.network

import com.tinker.movieinfo.BuildConfig
import com.tinker.movieinfo.model.MovieDetailApiResponse
import com.tinker.movieinfo.model.SearchApiResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

private const val API_KEY = BuildConfig.API_KEY

interface ApiEndpoint {

    @GET(".")
    suspend fun getMovies(@Query("s") searchTerm: String, @Query("apikey") apiKey: String = API_KEY): Response<SearchApiResponse>

    @GET(".")
    suspend fun getMovieDetails(@Query("i") imdbId: String, @Query("apikey") apiKey: String = API_KEY): Response<MovieDetailApiResponse>
}
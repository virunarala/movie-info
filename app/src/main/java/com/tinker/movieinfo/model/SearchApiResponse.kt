package com.tinker.movieinfo.model

import com.squareup.moshi.Json
import com.tinker.movieinfo.model.Movie

data class SearchApiResponse(

    @Json(name = "Search")
    val searchResult: List<Movie>,

    @Json(name = "totalResults")
    val resultsSize: String,

    @Json(name = "Response")
    val response: String,

    @Json(name = "Error")
    val errorMessage: String? = null,
)
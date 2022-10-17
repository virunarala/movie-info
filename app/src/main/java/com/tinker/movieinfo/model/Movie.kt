package com.tinker.movieinfo.model

import com.squareup.moshi.Json

/**
 * "Title": "Nói albinói",
"Year": "2003",
"imdbID": "tt0351461",
"Type": "movie",
"Poster": "https://m.media-amazon.com/images/M/MV5BOTQxNTBlOWItYWJhNS00ZmRlLWIzM2UtYWEwY2ExMDBjNmQ2L2ltYWdlL2ltYWdlXkEyXkFqcGdeQXVyMjgyNjk3MzE@._V1_SX300.jpg"
}
 */
data class Movie(

    @Json(name = "Title")
    val title: String?,

    @Json(name = "Year")
    val year: String?,

    @Json(name = "imdbID")
    val imdbId: String,

    @Json(name = "Type")
    val type: String?,

    @Json(name = "Poster")
    val posterUrl: String?,
)
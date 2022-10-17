package com.tinker.movieinfo.model

import com.squareup.moshi.Json

data class ErrorResponse(

    @Json(name = "Response")
    val response: String,

    @Json(name = "Error")
    val errorMessage: String? = null,
)

/**
 * "Title": "Nói albinói",
"Year": "2003",
"imdbID": "tt0351461",
"Type": "movie",
"Poster": "https://m.media-amazon.com/images/M/MV5BOTQxNTBlOWItYWJhNS00ZmRlLWIzM2UtYWEwY2ExMDBjNmQ2L2ltYWdlL2ltYWdlXkEyXkFqcGdeQXVyMjgyNjk3MzE@._V1_SX300.jpg"
}
 */


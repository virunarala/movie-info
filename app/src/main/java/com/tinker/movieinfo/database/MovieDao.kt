package com.tinker.movieinfo.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.tinker.movieinfo.constants.Database.TABLE_NAME_MOVIE
import com.tinker.movieinfo.model.Movie
import kotlinx.coroutines.flow.Flow

@Dao
abstract interface MovieDao {

    /**
     * Adds a Movie into the database
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMovie(movie: MovieEntity)

    /**
     * Returns a list of movies in the order of their search. Recent searches appear at top.
     */
    @Query("SELECT * FROM $TABLE_NAME_MOVIE ORDER BY createdAt DESC")
    suspend fun getAllMovies(): List<MovieEntity>

    /**
     * Clears all the data in the Movies table
     */
    @Query("DELETE FROM $TABLE_NAME_MOVIE")
    suspend fun clearAllMovies()

}
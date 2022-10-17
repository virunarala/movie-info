package com.tinker.movieinfo.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.tinker.movieinfo.constants.Database.TABLE_NAME_MOVIE

@Entity(tableName = TABLE_NAME_MOVIE)
data class MovieEntity(

    @PrimaryKey
    val imdbId: String,

    val title: String?,

    val year: String?,

    val type: String?,

    val posterUrl: String?,

    val createdAt: Long = System.currentTimeMillis()
)
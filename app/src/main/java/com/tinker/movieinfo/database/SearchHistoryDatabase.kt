package com.tinker.movieinfo.database

import androidx.room.Database
import androidx.room.RoomDatabase


@Database(entities = [MovieEntity::class],version = 1, exportSchema = false)
abstract class SearchHistoryDatabase: RoomDatabase() {
    abstract val movieDao: MovieDao
}
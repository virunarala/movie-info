package com.tinker.movieinfo.database

import android.content.Context
import androidx.room.Room
import com.tinker.movieinfo.constants.Database.DATABASE_NAME
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object DatabaseModule {

    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext context: Context): SearchHistoryDatabase {
        return Room.databaseBuilder(
            context,
            SearchHistoryDatabase::class.java,
            DATABASE_NAME
        ).fallbackToDestructiveMigration()
            .build()
    }

    @Singleton
    @Provides
    fun provideMovieDao(searchHistoryDatabase: SearchHistoryDatabase): MovieDao {
        return searchHistoryDatabase.movieDao
    }
}
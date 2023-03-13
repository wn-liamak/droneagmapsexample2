package com.liamakakpo.droneagmapsexample2.di

import android.content.Context
import com.liamakakpo.mapsexample.feature_database.CoordinatesDatabase
import com.liamakakpo.mapsexample.feature_database.DatabaseRepository
import com.liamakakpo.mapsexample.feature_database.dao.CoordinatesDao
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
    fun provideAppDatabase(coordinatesDao: CoordinatesDao): DatabaseRepository {
        return DatabaseRepository(coordinatesDao)
    }

    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext context: Context): CoordinatesDatabase {
        return CoordinatesDatabase.getInstance(context)
    }

    @Singleton
    @Provides
    fun provideCoordinatesDao(database: CoordinatesDatabase): CoordinatesDao {
        return database.coordinatesDao()
    }
}
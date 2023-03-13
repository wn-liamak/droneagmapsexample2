package com.liamakakpo.droneagmapsexample2.di

import android.content.Context
import android.content.SharedPreferences
import com.liamakakpo.droneagmapsexample2.preferences.MapPreferences
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

const val SHARED_PREFERENCES_FILE_NAME = "general_prefs.xml"

@InstallIn(SingletonComponent::class)
@Module
object PreferencesModule {

    @Provides
    @Singleton
    fun provideSharedPreferences(@ApplicationContext appContext: Context): SharedPreferences {
        return appContext.getSharedPreferences(SHARED_PREFERENCES_FILE_NAME, Context.MODE_PRIVATE)
    }

    @Provides
    @Singleton
    fun provideBasePreferences(sharedPreferences: SharedPreferences): MapPreferences {
        return MapPreferences(sharedPreferences)
    }
}
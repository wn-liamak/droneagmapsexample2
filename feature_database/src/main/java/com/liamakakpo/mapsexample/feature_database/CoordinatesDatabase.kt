package com.liamakakpo.mapsexample.feature_database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.liamakakpo.mapsexample.feature_database.dao.CoordinatesDao
import com.liamakakpo.mapsexample.feature_database.models.CoordinateEntry

const val LOCAL_DB_VERSION = 1

@Database(
    entities = [
        CoordinateEntry::class
    ], version = LOCAL_DB_VERSION
)
abstract class CoordinatesDatabase : RoomDatabase() {

    abstract fun coordinatesDao(): CoordinatesDao

    companion object {

        @Volatile
        private var INSTANCE: CoordinatesDatabase? = null

        private const val databaseName = "coords.db"

        fun getInstance(context: Context): CoordinatesDatabase =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: buildDatabase(context).also { INSTANCE = it }
            }

        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                CoordinatesDatabase::class.java,
                databaseName
            ).fallbackToDestructiveMigration() // just for this example project
                .build()
    }
}
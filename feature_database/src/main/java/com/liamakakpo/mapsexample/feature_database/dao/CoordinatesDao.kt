package com.liamakakpo.mapsexample.feature_database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.liamakakpo.mapsexample.feature_database.models.CoordinateEntry

@Dao
interface CoordinatesDao {

    @Query("SELECT * FROM coordinate")
    suspend fun getAllCoordinates(): List<CoordinateEntry>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(coordinateEntry: CoordinateEntry)

    @Query("DELETE FROM coordinate")
    suspend fun deleteAllCoordinates()
}
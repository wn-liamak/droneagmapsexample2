package com.liamakakpo.mapsexample.feature_database

import com.liamakakpo.mapsexample.feature_database.dao.CoordinatesDao
import com.liamakakpo.mapsexample.feature_database.models.CoordinateEntry

class DatabaseRepository(private val dao: CoordinatesDao) {

    suspend fun getAllCoordinates() =
        dao.getAllCoordinates()

    suspend fun insert(coordinateEntry: CoordinateEntry) =
        dao.insert(coordinateEntry)

    suspend fun deleteAllCoordinates() =
        dao.deleteAllCoordinates()
}
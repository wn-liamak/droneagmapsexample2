package com.liamakakpo.mapsexample.feature_database.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "coordinate")
data class CoordinateEntry(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val latitude: Double,
    val longitude: Double
)
package com.liamakakpo.droneagmapsexample2.ui.main

import com.google.android.gms.maps.CameraUpdate
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds

data class MapUiModel(
    val cameraUpdate: CameraUpdate? = null,
    val coordinates: List<LatLng> = emptyList(),
    val areaKilometers: Double = 0.0
) {
    fun getPolygonCenter() = when {
        coordinates.isEmpty() -> null
        coordinates.size == 1 -> coordinates.first()
        else -> LatLngBounds.Builder().apply {
            for (w in coordinates) {
                include(w)
            }
        }.build().center
    }
}
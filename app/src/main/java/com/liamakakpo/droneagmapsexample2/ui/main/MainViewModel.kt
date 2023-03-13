package com.liamakakpo.droneagmapsexample2.ui.main

import androidx.lifecycle.ViewModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.maps.android.SphericalUtil
import com.liamakakpo.droneagmapsexample2.preferences.MapPreferences
import com.liamakakpo.mapsexample.feature_database.DatabaseRepository
import com.liamakakpo.mapsexample.feature_database.models.CoordinateEntry
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val preferences: MapPreferences,
    private val databaseRepository: DatabaseRepository
) : ViewModel() {

    companion object {
        private const val LAT_LNG_BOUNDS_PADDING = 35
    }

    private val mutableViewState: MutableStateFlow<MapUiModel> =
        MutableStateFlow(MapUiModel())
    val viewState: StateFlow<MapUiModel> = mutableViewState

    private var initialised = false

    fun initialise() {
        CoroutineScope(Dispatchers.IO).launch {
            val savedCoordinates = databaseRepository.getAllCoordinates().map {
                LatLng(it.latitude, it.longitude)
            }
            val cameraUpdate =
                if (savedCoordinates.size < 2) { // initialise camera position to default
                    CameraUpdateFactory.newLatLngZoom(
                        preferences.lastPosition,
                        preferences.lastZoom,
                    )
                } else {  // initialise camera position to existing polygon
                    val bounds = LatLngBounds.Builder().apply {
                        for (w in savedCoordinates) {
                            include(w)
                        }
                    }.build()
                    CameraUpdateFactory.newLatLngBounds(bounds, LAT_LNG_BOUNDS_PADDING)
                }
            mutableViewState.value = MapUiModel(
                cameraUpdate = cameraUpdate,
                coordinates = savedCoordinates,
                areaKilometers = getAreaKilometers(savedCoordinates)
            )
            initialised = true
        }
    }

    fun addCoordinate(latLng: LatLng) {
        val coordinates = mutableListOf<LatLng>() // new instance must be made to refresh view state
        coordinates.addAll(mutableViewState.value.coordinates)
        coordinates.add(latLng)
        mutableViewState.value = mutableViewState.value.copy(  // update view state
            cameraUpdate = null,
            coordinates = coordinates,
            areaKilometers = getAreaKilometers(coordinates)
        )
        CoroutineScope(Dispatchers.IO).launch {
            databaseRepository.insert(  // save new coordinates to database
                CoordinateEntry(
                    latitude = latLng.latitude,
                    longitude = latLng.longitude
                )
            )
        }
    }

    fun onMapMoved(target: LatLng, zoom: Float) {
        if (!initialised) return
        preferences.apply {
            lastPosition = target
            lastZoom = zoom
        }
    }

    fun clearPolygon() {
        mutableViewState.value = // update view state
            MapUiModel(
                cameraUpdate = null,
                coordinates = emptyList(),
            )
        CoroutineScope(Dispatchers.IO).launch {
            databaseRepository.deleteAllCoordinates()
        }
    }

    private fun getAreaKilometers(coordinates: List<LatLng>) =
        if (coordinates.size > 1) SphericalUtil.computeArea(coordinates) / 1000.0 else 0.0
}
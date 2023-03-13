package com.liamakakpo.droneagmapsexample2.preferences

import android.content.SharedPreferences
import com.google.android.gms.maps.model.LatLng


class MapPreferences(val preferences: SharedPreferences) {

    companion object {
        private const val MAP_LATITUDE = "last_map_latitude"
        private const val MAP_LONGITUDE = "last_map_longitude"

        private const val MAP_ZOOM = "last_map_zoom"

        private const val DEFAULT_LAT_LONDON = "51.5282145"
        private const val DEFAULT_LNG_LONDON = "-0.0864924"
        const val DEFAULT_ZOOM = 11f
    }

    // i've stored latitude and longitude as converted strings, since
    // shared preferences can store floats but not doubles, and we do not
    // want to lose precision
    var lastPosition: LatLng
        get() {
            val lat = preferences.getString(MAP_LATITUDE, DEFAULT_LAT_LONDON)!!.toDouble()
            val lng = preferences.getString(MAP_LONGITUDE, DEFAULT_LNG_LONDON)!!.toDouble()
            return LatLng(lat, lng)
        }
        set(value) {
            preferences.edit()
                .putString(MAP_LATITUDE, value.latitude.toString())
                .putString(MAP_LONGITUDE, value.longitude.toString())
                .apply()
        }

    var lastZoom: Float
        get() = preferences.getFloat(MAP_ZOOM, DEFAULT_ZOOM)
        set(value) = preferences.edit()
            .putFloat(MAP_ZOOM, value)
            .apply()
}
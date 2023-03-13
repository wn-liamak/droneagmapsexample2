package com.liamakakpo.droneagmapsexample2.markers

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Matrix
import androidx.core.content.ContextCompat
import com.liamakakpo.droneagmapsexample2.R


object MarkerFactory {

    fun getMarker(context: Context): Bitmap? {
        val vectorDrawable = ContextCompat.getDrawable(context,  R.drawable.ic_location_marker_green_24dp)
        vectorDrawable!!.setBounds(
            0,
            0,
            vectorDrawable.intrinsicWidth,
            vectorDrawable.intrinsicHeight
        )
        val bitmap = Bitmap.createBitmap(
            vectorDrawable.intrinsicWidth,
            vectorDrawable.intrinsicHeight,
            Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(bitmap)
        vectorDrawable.draw(canvas)
        val px = context.resources.getDimensionPixelSize(R.dimen.marker_size_location)
        val matrix = Matrix()
        val scaledBitmap = Bitmap.createScaledBitmap(bitmap, px, px, true)
        val marker = Bitmap.createBitmap(
            scaledBitmap,
            0,
            0,
            scaledBitmap.width,
            scaledBitmap.height,
            matrix,
            true
        )
        bitmap.recycle()
        scaledBitmap.recycle()
        return marker
    }
}
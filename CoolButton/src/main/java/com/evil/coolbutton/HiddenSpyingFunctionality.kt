package com.evil.coolbutton

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.LocationListener
import android.location.LocationManager
import android.util.Log

fun trackUserLocationIfAvailable(context: Context) {
    if (context.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
        val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        var locationListener: LocationListener? = null
        locationListener = LocationListener {
            Log.i("evil", "ha-ha-ha, I know where you live ${it.latitude} ${it.longitude}")
            locationManager.removeUpdates(locationListener!!)
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 1f, locationListener)
    }
}
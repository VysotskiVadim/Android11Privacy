package by.android.academy.privacy

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import androidx.annotation.RequiresPermission
import androidx.core.content.ContextCompat
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

val Context.gpsLocation: Flow<Location>
    @RequiresPermission(Manifest.permission.ACCESS_FINE_LOCATION) get() = callbackFlow {
        val locationCallback = object : LocationListener {
            override fun onLocationChanged(p0: Location) {
                offer(p0)
            }

            override fun toString(): String {
                return "haha"
            }
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10 * 1000, 1f, locationCallback)
        awaitClose {
            locationManager.removeUpdates(locationCallback)
        }
    }

@RequiresPermission(Manifest.permission.ACCESS_FINE_LOCATION)
fun Context.currentGPSLocation(): Location? = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)

private val Context.locationManager get() = getSystemService(Context.LOCATION_SERVICE) as LocationManager

fun Context.showLocation(): String {
    val isGranted = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
    return if (isGranted) {
        val currentLocation = currentGPSLocation()
        formatLocation(currentLocation)
    } else {
        ""
    }
}

fun Context.formatLocation(location: Location?): String {
    return if (location != null) {
        getString(R.string.userLocation, location.latitude, location.longitude)
    } else {
        getString(R.string.unableToGetLocation)
    }
}
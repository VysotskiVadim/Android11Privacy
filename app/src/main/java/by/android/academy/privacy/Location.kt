package by.android.academy.privacy

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import androidx.annotation.RequiresPermission
import androidx.core.content.ContextCompat
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

val Context.userLocation: Flow<Location>
    @RequiresPermission(Manifest.permission.ACCESS_FINE_LOCATION) get() {
        val context = this
        return callbackFlow {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10 * 1000, 1f) { location ->
                offer(location)
            }
        }
    }

@RequiresPermission(Manifest.permission.ACCESS_FINE_LOCATION)
fun Context.currentGPSLocation(): Location? = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)

private val Context.locationManager get() = getSystemService(Context.LOCATION_SERVICE) as LocationManager

fun Context.formatLocation(): String {
    val isGranted = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
    return if (isGranted) {
        val currentLocation = currentGPSLocation()
        if (currentLocation != null) {
            getString(R.string.userLocation, currentLocation.latitude, currentLocation.longitude)
        } else {
            getString(R.string.unableToGetLocation)
        }
    } else {
        ""
    }
}
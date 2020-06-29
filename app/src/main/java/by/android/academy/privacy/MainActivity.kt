package by.android.academy.privacy

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_main.*

private const val LOCATION_PERMISSION_REQUEST_CODE = 123

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        showCurrentPermissionStatus()
        updateLocation()
        locationButton.setOnClickListener {
            when {
                checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) ==
                        PackageManager.PERMISSION_GRANTED -> {
                    // You can use the API that requires the permission.
                    permissionStatus.text = getText(R.string.locationPermissionGranted)
                }
                shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION) && permissionStatus.text != getText(R.string.educationalContent) -> {
                    // In an educational UI, explain to the user why your app requires this
                    // permission for a specific feature to behave as expected.
                    permissionStatus.text = getText(R.string.educationalContent)
                }
                else -> {
                    // You can directly ask for the permission.
                    requestPermissions(
                        arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                        LOCATION_PERMISSION_REQUEST_CODE
                    )
                }
            }
        }
    }

    override fun onResume() {
        showCurrentPermissionStatus()
        super.onResume()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)
        ) {
            permissionStatus.text = getText(R.string.locationPermissionGranted)
            permissionStatus.setTextColor(getColor(R.color.accessGrantedColor))
            updateLocation()
        } else {
            permissionStatus.text = getText(R.string.locationPermissionDeclined)
            permissionStatus.setTextColor(getColor(R.color.accessDeniedColor))
        }
    }

    private fun showCurrentPermissionStatus() {
        val isGranted = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
        if (isGranted) {
            permissionStatus.text = getText(R.string.locationPermissionGranted)
            permissionStatus.setTextColor(getColor(R.color.accessGrantedColor))
        } else {
            permissionStatus.text = getText(R.string.locationPermissionNotGranted)
            permissionStatus.setTextColor(getColor(R.color.accessDeniedColor))
        }
    }

    private fun updateLocation() {
        val isGranted = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
        if (isGranted) {
            val currentLocation = currentGPSLocation()
            if (currentLocation != null) {
                location.text = "lat: ${currentLocation.latitude}, long: ${currentLocation.longitude}"
            } else {
                location.text = "unable to get location"
            }
        } else {
            location.text = ""
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        super.onCreateOptionsMenu(menu)
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.startForeground -> {
                ContextCompat.startForegroundService(this, Intent(this, ForegroudService::class.java))
                return true
            }
            else -> super.onContextItemSelected(item)
        }
    }
}

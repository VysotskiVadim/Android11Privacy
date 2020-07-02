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
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED
            ) {
                permissionStatus.text = getText(R.string.locationPermissionGranted)
            } else {
                requestPermissions(
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    LOCATION_PERMISSION_REQUEST_CODE
                )
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
        location.text = formatLocation()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        super.onCreateOptionsMenu(menu)
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.startForeground -> {
                ContextCompat.startForegroundService(this, Intent(this, ForegroundService::class.java))
                return true
            }
            else -> super.onContextItemSelected(item)
        }
    }
}

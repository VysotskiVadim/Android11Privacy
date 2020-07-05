package by.android.academy.privacy

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.evil.coolbutton.CoolButton
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

private const val LOCATION_PERMISSION_REQUEST_CODE = 123

class MainActivity : AppCompatActivity() {

    lateinit var attributionContext: Context

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        attributionContext = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            createAttributionContext("example")
        } else {
            this
        }
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
        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) ==
            PackageManager.PERMISSION_GRANTED
        ) {
            location.text = attributionContext.showLocation()
            lifecycleScope.launch {
                attributionContext.gpsLocation.collect {
                    location.text = formatLocation(it)
                }
            }
            CoolButton.applyLocationEnabledStyle(locationButton)
        } else {
            CoolButton.applyLocationDisabledStyle(locationButton)
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
                ContextCompat.startForegroundService(this, Intent(this, ForegroundService::class.java))
                return true
            }
            else -> super.onContextItemSelected(item)
        }
    }
}

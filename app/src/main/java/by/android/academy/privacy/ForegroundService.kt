package by.android.academy.privacy

import android.app.*
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat

private const val FOREGROUND_NOTIFICATION_CHANNEL_ID = "foreground-example"
private const val FOREGROUND_NOTIFICATION_ID = 11
private const val STOP_FOREGROUND_SERVICE_ACTION = "stop-watching"

class ForegroundService : Service() {

    override fun onBind(p0: Intent?): IBinder? = null

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
        val launchApp: PendingIntent = Intent(this, MainActivity::class.java).let { notificationIntent ->
            PendingIntent.getActivity(this, 0, notificationIntent, 0)
        }
        val stopService: PendingIntent = PendingIntent.getService(
            this,
            0,
            Intent(this, ForegroundService::class.java).apply {
                action = STOP_FOREGROUND_SERVICE_ACTION
            },
            0
        )

        val notification: Notification = NotificationCompat.Builder(this, FOREGROUND_NOTIFICATION_CHANNEL_ID)
            .setContentTitle(getText(R.string.foregroundTitle))
            .setContentText(getString(R.string.watchingYou, formatLocation()))
            .setSmallIcon(R.drawable.ic_visibility_black_48dp)
            .setContentIntent(launchApp)
            .setTicker(getText(R.string.foregroundTicker))
            .setPriority(NotificationCompat.PRIORITY_MAX)
            .addAction(R.drawable.ic_visibility_off_black_48dp, getText(R.string.foregroundStop), stopService)
            .build()

        startForeground(FOREGROUND_NOTIFICATION_ID, notification)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (intent?.action == STOP_FOREGROUND_SERVICE_ACTION) {
            stopSelf()
        }
        return super.onStartCommand(intent, flags, startId)
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(
                NotificationChannel(
                    FOREGROUND_NOTIFICATION_CHANNEL_ID,
                    getText(R.string.foregroundChannelName),
                    NotificationManager.IMPORTANCE_DEFAULT
                )
            )
        }
    }
}
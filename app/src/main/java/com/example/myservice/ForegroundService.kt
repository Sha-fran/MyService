package com.example.myservice

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.os.IBinder
import androidx.core.app.NotificationCompat
import kotlin.concurrent.thread

class ForegroundService: Service() {
    var pendingIntent:PendingIntent? = null
    override fun onBind(intent: Intent?): IBinder? = null

    override fun onCreate() {
        super.onCreate()
        val intent = Intent(baseContext, MainActivity::class.java)
        pendingIntent = PendingIntent.getActivity(baseContext, 0, intent, PendingIntent.FLAG_MUTABLE)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val channel = NotificationChannel(CHANNEL_NOTIFICATION_ID, "MyForegroundChannel", NotificationManager.IMPORTANCE_DEFAULT)
        val manager = getSystemService(NotificationManager::class.java)
        manager.createNotificationChannel(channel)
        val builder = getNotificationBuilder()
        startForeground(1, builder.build())

        thread {
            repeat(101) {
                Thread.sleep(500)
                builder.setProgress(100, it, false)
                builder.setContentText("$it%")
                manager.notify(1, builder.build())
            }
        }

        return START_STICKY
    }

    private fun getNotificationBuilder():NotificationCompat.Builder {

        return NotificationCompat.Builder(baseContext, CHANNEL_NOTIFICATION_ID)
            .setContentTitle("Foreground Service")
            .setOnlyAlertOnce(true)
            .setSmallIcon(android.R.drawable.ic_dialog_alert)
            .setContentIntent(pendingIntent)
    }

    companion object {
        const val CHANNEL_NOTIFICATION_ID = "channel_notification_id"
    }
}

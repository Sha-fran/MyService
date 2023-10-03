package com.example.myservice

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import kotlin.concurrent.thread

class MyService: Service() {

    override fun onCreate() {
        super.onCreate()
        Log.e("MyService", "onCreate")
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        var message = ""
        intent.let {
            message = intent?.getStringExtra("Message")?:""
        }

        if (message.isNotEmpty()) {
            thread {
                Thread.sleep(3000)
                message = message.uppercase()
                val broadcastIntent = Intent()
                broadcastIntent.action = SERVICE_MESSAGE
                broadcastIntent.putExtra(UPPERCASE_MESSAGE, message)
                sendBroadcast(broadcastIntent)
            }
        }

        Log.e("MyService", "onStartCommand message = ${message}")
        return Service.START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.e("MyService", "onDestroy")
    }

    override fun onBind(p0: Intent?): IBinder? = null

    companion object {
        const val SERVICE_MESSAGE = "service_message"
        const val UPPERCASE_MESSAGE = "uppercase_message"
    }
}

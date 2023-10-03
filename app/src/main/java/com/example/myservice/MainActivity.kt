package com.example.myservice

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.ServiceConnection
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.IBinder
import android.widget.Button
import android.widget.EditText
import androidx.core.app.NotificationCompat

class MainActivity : AppCompatActivity() {
    lateinit var inputField:EditText
    var boundService: BoundService? = null
    var binder:BoundService.MyBinder? = null

    private val connection = object:ServiceConnection{
        override fun onServiceConnected(p0: ComponentName?, service: IBinder?) {
            binder = service as? BoundService.MyBinder
            boundService = binder?.getService()
        }

        override fun onServiceDisconnected(p0: ComponentName?) {
            binder = null
            boundService = null
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        bindService(Intent(this, BoundService::class.java), connection, Context.BIND_AUTO_CREATE)

        inputField = findViewById(R.id.edit_text)
        val button:Button = findViewById(R.id.button)

        button.setOnClickListener{
            startService(Intent(this, ForegroundService::class.java))
//            showNotification()
            boundService?.let {
                inputField.setText(it.transformString(inputField.text.toString()))
            }
//            val intent = Intent(this, MyService::class.java)
//            intent.putExtra("Message", "${inputField.text}")
//            startService(intent)
        }

//        val myBroadcastReceiver = MyBroadcastReceiver{
//            inputField.setText(it)
//        }

//        val intentFilter = IntentFilter(MyService.SERVICE_MESSAGE)
//        registerReceiver(myBroadcastReceiver, intentFilter)
//    }

//    class MyBroadcastReceiver (val onResult:(result:String) -> Unit) : BroadcastReceiver() {
//        override fun onReceive(context: Context?, intent: Intent?) {
//            intent?.let {
//                if (intent.action == MyService.SERVICE_MESSAGE) {
//                    val message = it.getStringExtra(MyService.UPPERCASE_MESSAGE) ?:""
//                    if (message.isNotEmpty()) {
//                        onResult(message)
//                    }
//                }
//            }
//        }
    }

    fun showNotification() {
        val channel = NotificationChannel("MyChannelId", "MyForegroundChannel", NotificationManager.IMPORTANCE_DEFAULT)
        val manager = getSystemService(NotificationManager::class.java)
        manager.createNotificationChannel(channel)
        val notification = NotificationCompat.Builder(baseContext, "MyChannelId")
            .setContentTitle("Mainactivity notification")
            .setContentText("My notification")
            .setSmallIcon(android.R.drawable.ic_dialog_alert)
            .build()

        manager.notify(1, notification)
    }
}

package com.example.myservice

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder

class BoundService:Service() {
    private val binder = MyBinder()

    override fun onBind(intent: Intent?): IBinder = binder

    fun transformString(messageString: String) = messageString.uppercase()

    inner class MyBinder : Binder(){
        fun getService() = this@BoundService
    }
}

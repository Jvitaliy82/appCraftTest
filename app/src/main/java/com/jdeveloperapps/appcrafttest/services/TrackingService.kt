package com.jdeveloperapps.appcrafttest.services

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.NotificationManager.IMPORTANCE_LOW
import android.app.PendingIntent
import android.app.PendingIntent.FLAG_UPDATE_CURRENT
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.lifecycle.LifecycleService
import com.jdeveloperapps.appcrafttest.R
import com.jdeveloperapps.appcrafttest.ui.MainActivity
import com.jdeveloperapps.appcrafttest.util.Constants.ACTION_PAUSE_SERVICE
import com.jdeveloperapps.appcrafttest.util.Constants.ACTION_SHOW_LOCATION_FRAGMENT
import com.jdeveloperapps.appcrafttest.util.Constants.ACTION_START_OR_RESUME_SERVICE
import com.jdeveloperapps.appcrafttest.util.Constants.ACTION_STOP_SERVICE
import com.jdeveloperapps.appcrafttest.util.Constants.NOTIFICATION_CHANNEL_ID
import com.jdeveloperapps.appcrafttest.util.Constants.NOTIFICATION_CHANNEL_NAME
import com.jdeveloperapps.appcrafttest.util.Constants.NOTIFICATION_ID

class TrackingService : LifecycleService() {

    var isFirsRun = true

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        intent?.let {
            when (it.action) {
                ACTION_START_OR_RESUME_SERVICE -> {
                    Log.d("M1", "start or resume service")
                    if (isFirsRun) {
                        startForegroundService()
                        isFirsRun = false
                    } else {

                    }
                }
                ACTION_PAUSE_SERVICE -> {
                    Log.d("M1", "pause service")
                }
                ACTION_STOP_SERVICE -> {
                    Log.d("M1", "stop service")
                }
                else -> {
                }
            }
        }
        return super.onStartCommand(intent, flags, startId)
    }

    private fun startForegroundService() {
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel(notificationManager)
        }

        val notificationBuilder = NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
            .setAutoCancel(false)
            .setOngoing(true)
            .setSmallIcon(R.drawable.ic_location)
            .setContentTitle("Я слежу за тобой ;)")
            .setContentText("ваши координаты: ")
            .setContentIntent(getMainActivityPendingIntent())

        startForeground(NOTIFICATION_ID, notificationBuilder.build())
    }

    private fun getMainActivityPendingIntent() = PendingIntent.getActivity(
        this,
        0,
        Intent(this, MainActivity::class.java).also {
            it.action = ACTION_SHOW_LOCATION_FRAGMENT
        },
        FLAG_UPDATE_CURRENT
    )

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel(notificationManager: NotificationManager) {
        val channel = NotificationChannel(
            NOTIFICATION_CHANNEL_ID, NOTIFICATION_CHANNEL_NAME,
            IMPORTANCE_LOW
        )
        notificationManager.createNotificationChannel(channel)
    }

}
package com.jdeveloperapps.appcrafttest.services

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.NotificationManager.IMPORTANCE_LOW
import android.app.PendingIntent
import android.app.PendingIntent.FLAG_UPDATE_CURRENT
import android.content.Context
import android.content.Intent
import android.location.Location
import android.os.Build
import android.os.Looper
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.lifecycle.LifecycleService
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.jdeveloperapps.appcrafttest.R
import com.jdeveloperapps.appcrafttest.ui.MainActivity
import com.jdeveloperapps.appcrafttest.util.Constants.ACTION_PAUSE_SERVICE
import com.jdeveloperapps.appcrafttest.util.Constants.ACTION_SHOW_LOCATION_FRAGMENT
import com.jdeveloperapps.appcrafttest.util.Constants.ACTION_START_OR_RESUME_SERVICE
import com.jdeveloperapps.appcrafttest.util.Constants.ACTION_STOP_SERVICE
import com.jdeveloperapps.appcrafttest.util.Constants.FASTEST_LOCATION_INTERVAL
import com.jdeveloperapps.appcrafttest.util.Constants.LOCATION_UPDATE_INTERVAL
import com.jdeveloperapps.appcrafttest.util.Constants.NOTIFICATION_CHANNEL_ID
import com.jdeveloperapps.appcrafttest.util.Constants.NOTIFICATION_CHANNEL_NAME
import com.jdeveloperapps.appcrafttest.util.Constants.NOTIFICATION_ID
import com.jdeveloperapps.appcrafttest.util.TrackingUtility

class TrackingService : LifecycleService() {

    lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    companion object {
        val isTracking = MutableLiveData<Boolean>()
        val userLocation = MutableLiveData<Location>()
    }

    override fun onCreate() {
        super.onCreate()
        fusedLocationProviderClient = FusedLocationProviderClient(this)

        isTracking.observe(this) {
            updateLocationTracking(it)
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        intent?.let {
            when (it.action) {
                ACTION_START_OR_RESUME_SERVICE -> {
                    Log.d("M1", "start or resume service")
                    startForegroundService()
                }
                ACTION_STOP_SERVICE -> {
                    Log.d("M1", "stop service")
                    killService()
                }
                else -> {

                }
            }
        }
        return super.onStartCommand(intent, flags, startId)
    }

    @SuppressLint("MissingPermission")
    private fun updateLocationTracking(isTracking: Boolean) {
        if (isTracking) {
            if (TrackingUtility.hasLocationPermission(this)) {
                val request = LocationRequest().apply {
                    interval = LOCATION_UPDATE_INTERVAL
                    fastestInterval = FASTEST_LOCATION_INTERVAL
                    priority = LocationRequest.PRIORITY_HIGH_ACCURACY
                }
                fusedLocationProviderClient.requestLocationUpdates(
                    request,
                    locationCallback,
                    Looper.getMainLooper()
                )
            }
        } else {
            fusedLocationProviderClient.removeLocationUpdates(locationCallback)
        }
    }

    val locationCallback = object : LocationCallback() {
        override fun onLocationResult(result: LocationResult?) {
            super.onLocationResult(result)
            if (isTracking.value!!) {
                result?.lastLocation?.let {
                    userLocation.postValue(it)
                    Log.d("M1", "lat:${it.latitude} long:${it.longitude}")
                }
            }
        }
    }

    private fun startForegroundService() {

        isTracking.postValue(true)

        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel(notificationManager)
        }

        val notificationBuilder = NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
            .setAutoCancel(false)
            .setOngoing(true)
            .setSmallIcon(R.drawable.ic_location)
            .setContentTitle("Я слежу за тобой ;)")
            .setContentText("не забудь нажать Stop")
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

    private fun killService() {
        isTracking.postValue(false)
        stopForeground(true)
        stopSelf()
    }

}
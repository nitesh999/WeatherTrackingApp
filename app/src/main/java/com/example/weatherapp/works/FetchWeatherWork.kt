package com.example.weatherapp.works

import android.content.Context
import android.location.Location
import android.os.Looper
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.weatherapp.WeatherApplication
import com.example.weatherapp.util.Constants
import com.google.android.gms.location.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class FetchWeatherWork(private val mContext: Context, params: WorkerParameters) : Worker(mContext, params) {

    private var mFusedLocationClient: FusedLocationProviderClient? = null
    private lateinit var mLocationCallback: LocationCallback
    private val TAG = "FetchWeatherWork"
    override fun doWork(): Result {
        try {
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(mContext)
            mLocationCallback = object : LocationCallback() {
                override fun onLocationResult(locationResult: LocationResult) {
                    super.onLocationResult(locationResult)
                    val location: Location = locationResult.lastLocation
                    CoroutineScope(Dispatchers.IO).launch {
                        WeatherApplication.appComponent.getNetworkHelper().weatherApi
                            .getWeather(location.latitude, location.longitude, Constants.API_KEY)
                    }
                }
            }

            val mLocationRequest = LocationRequest.create().apply {
                priority = LocationRequest.PRIORITY_HIGH_ACCURACY
                fastestInterval = 0
                numUpdates = 1
            }

            try {
                mFusedLocationClient?.lastLocation
                    ?.addOnCompleteListener { task ->
                        val location: Location? = task.result
                        if (task.isSuccessful && location != null) {
                            CoroutineScope(Dispatchers.IO).launch {
                                WeatherApplication.appComponent.getNetworkHelper().weatherApi
                                    .getWeather(location.latitude, location.longitude, Constants.API_KEY)
                            }
                            Log.d(TAG, "Location : $location")
                        } else {
                            mFusedLocationClient!!.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.getMainLooper())
                            Log.w(TAG, "Failed to get location.")
                        }
                    }
            } catch (unlikely: SecurityException) {
                Log.e(TAG, "Lost location permission.$unlikely")
            }
        } catch (exception: Exception) {
            Result.failure()
        }
        return Result.success()
    }
}

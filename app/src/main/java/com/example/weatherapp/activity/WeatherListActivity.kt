package com.example.weatherapp.activity

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.os.Looper
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.chatapplication.adapter.WeatherListAdapter
import com.example.weatherapp.R
import com.google.android.gms.location.*
import kotlinx.android.synthetic.main.activity_weather_list.*
import android.provider.Settings
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.multidex.BuildConfig
import androidx.work.ExistingWorkPolicy
import com.example.weatherapp.viewmodel.WeatherLookupViewModel
import com.example.weatherapp.viewmodel.WeatherLookupViewModelFactory

class WeatherListActivity : AppCompatActivity() {

    var mFusedLocationClient: FusedLocationProviderClient? = null
    private val PERMISSION_ID:Int = 44
    val viewModel: WeatherLookupViewModel by viewModels { WeatherLookupViewModelFactory.getInstance() }

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_weather_list)

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        // method to get the location
        getLastLocation()

        val userListAdapter = WeatherListAdapter(onWeatherClick)
        rvWeather.apply {
            layoutManager = LinearLayoutManager(application)
            adapter = userListAdapter
        }
        viewModel.weatherDetails.observe(this, { weatherDetails ->
            userListAdapter.weatherList = weatherDetails.weather
        })
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    @SuppressLint("MissingPermission")
    private fun getLastLocation() {
        // check if permissions are given
        if (checkPermissions()) {
            // check if location is enabled
            if (isLocationEnabled()) {
                // getting last location from FusedLocationClient object
                mFusedLocationClient?.lastLocation
                    ?.addOnCompleteListener { task ->
                        val location: Location? = task.result
                        if (location == null) {
                            requestNewLocationData()
                        } else {
                            viewModel.lookup(location.latitude, location.longitude)
                        }
                    }
            } else {
                Toast.makeText(this, "Please turn on" + " your location...", Toast.LENGTH_LONG).show()
                val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                startActivity(intent)
            }
        } else {
            // if permissions aren't available,
            // request for permissions
            requestPermissions()
        }
    }

    @SuppressLint("MissingPermission")
    private fun requestNewLocationData() {
        // Initializing LocationRequest
        // object with appropriate methods
        val mLocationRequest = LocationRequest.create().apply {
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
            fastestInterval = 0
            numUpdates = 1
        }
        // setting LocationRequest
        // on FusedLocationClient
        mFusedLocationClient!!.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.getMainLooper())
    }

    private val mLocationCallback: LocationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            val mLastLocation: Location = locationResult.lastLocation
            viewModel.lookup(mLastLocation.latitude, mLastLocation.longitude)
        }
    }

    // method to check for permissions
    @RequiresApi(Build.VERSION_CODES.Q)
    private fun checkPermissions(): Boolean {
        val isPermitted = ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED

        if (BuildConfig.VERSION_CODE == Build.VERSION_CODES.Q)
            return isPermitted && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_BACKGROUND_LOCATION) == PackageManager.PERMISSION_GRANTED

        return isPermitted
    }

    // method to request for permissions
    @RequiresApi(Build.VERSION_CODES.Q)
    private fun requestPermissions() {
        val arrayPermission: Array<String> =
        if(BuildConfig.VERSION_CODE==Build.VERSION_CODES.Q)
            arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_BACKGROUND_LOCATION)
        else
            arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION)
        ActivityCompat.requestPermissions(this, arrayPermission, PERMISSION_ID)
    }

    // method to check
    // if location is enabled
    private fun isLocationEnabled(): Boolean {
        val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
    }

    // If everything is alright then
    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSION_ID) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLastLocation()
            }
        }
    }

    override fun onStop() {
        super.onStop()
        mFusedLocationClient!!.removeLocationUpdates(mLocationCallback)
    }

    private val onWeatherClick: (Int) -> Unit = { pos ->
        val intent = Intent(this, WeatherDetailsActivity::class.java)
        startActivity(intent)
    }
}
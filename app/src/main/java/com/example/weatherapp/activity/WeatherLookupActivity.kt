package com.example.weatherapp.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.example.weatherapp.R
import com.example.weatherapp.databinding.ActivityWeatherLookupBinding
import com.example.weatherapp.viewmodel.WeatherLookupViewModel

class WeatherLookupActivity : AppCompatActivity() {

    //lateinit var viewModel: WeatherLookupViewmodel
    val viewModel: WeatherLookupViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        /*DaggerActivityComponent.builder()
            .appComponent(WeatherApplication.appComponent)
            .build()
            .inject(this)*/

        val binding: ActivityWeatherLookupBinding = DataBindingUtil.setContentView(this, R.layout.activity_weather_lookup)

        binding.lifecycleOwner = this
        binding.executePendingBindings()

        viewModel.showStatus.observe(this, Observer<String> { weatherStatus ->
            if(!weatherStatus.isNullOrEmpty()) {
                Toast.makeText(this@WeatherLookupActivity, weatherStatus, Toast.LENGTH_LONG).show()
            }
        })
        /*viewModel.navWeatherListActivity.observe(this, { loginStatus ->
            val intent = Intent(this, WeatherListActivity::class.java)
            intent.putParcelableArrayListExtra("data",  viewModel.weatherList!!)
            startActivity(intent)
        })*/
    }
}
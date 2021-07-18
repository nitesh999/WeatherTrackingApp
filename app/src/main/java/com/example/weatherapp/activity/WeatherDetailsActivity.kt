package com.example.weatherapp.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.example.weatherapp.R
import com.example.weatherapp.databinding.ActivityWeatherDetailsBinding
import com.example.weatherapp.network.WeatherDetails
import com.example.weatherapp.viewmodel.WeatherDetailsViewmodel

class WeatherDetailsActivity : AppCompatActivity() {

    lateinit var viewModel: WeatherDetailsViewmodel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding: ActivityWeatherDetailsBinding = DataBindingUtil.setContentView(this,
            R.layout.activity_weather_details)

        val weather = intent.getParcelableExtra<WeatherDetails>("data")
        binding.lifecycleOwner = this
        binding.networkWeather = weather
        binding.executePendingBindings()
    }
}
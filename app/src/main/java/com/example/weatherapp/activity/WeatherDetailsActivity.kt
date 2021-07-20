package com.example.weatherapp.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import com.example.weatherapp.R
import com.example.weatherapp.databinding.ActivityWeatherDetailsBinding
import com.example.weatherapp.network.WeatherDetails
import com.example.weatherapp.viewmodel.WeatherLookupViewModel
import com.example.weatherapp.viewmodel.WeatherLookupViewModelFactory

class WeatherDetailsActivity : AppCompatActivity() {

    val viewModel: WeatherLookupViewModel by viewModels { WeatherLookupViewModelFactory.getInstance() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding: ActivityWeatherDetailsBinding = DataBindingUtil.setContentView(this,
            R.layout.activity_weather_details)

        binding.lifecycleOwner = this
        binding.networkWeather = viewModel.weatherDetails.value
        binding.executePendingBindings()
    }
}
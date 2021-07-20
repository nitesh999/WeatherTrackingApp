package com.example.weatherapp.inerfaces

import com.example.weatherapp.network.WeatherDetails
import retrofit2.http.*

interface WeatherAPI {

    @GET("data/2.5/weather?")
    suspend fun getWeather(@Query("lat") lat: Double, @Query("lon") lon: Double,
                           @Query("appid") apiKey: String): WeatherDetails
}
package com.example.weatherapp.network

import android.os.Parcelable
import com.squareup.moshi.JsonClass
import kotlinx.android.parcel.Parcelize

@Parcelize
@JsonClass(generateAdapter = true)
data class WeatherDetails(val cod: String, val weather: List<Weather>, val main: Main, val name:String) : Parcelable

@Parcelize
@JsonClass(generateAdapter = true)
data class Main(
    val temp: String,
    val feels_like: String,
    val humidity: String,
    val pressure: String
) : Parcelable

@Parcelize
@JsonClass(generateAdapter = true)
data class Weather(
    val main: String,
    val description: String
) : Parcelable




package com.example.weatherapp.viewmodel

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.weatherapp.base.BaseUTTest
import com.example.weatherapp.network.WeatherDetails
import com.example.weatherapp.repository.WeatherRepository
import com.squareup.moshi.Moshi
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.Dispatchers
import org.junit.*
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class WeatherLookupViewModelTest : BaseUTTest() {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @MockK
    private lateinit var weatherRepository: WeatherRepository

    @Before
    fun start(){
        super.setUp()
        MockKAnnotations.init(this)
    }

    @Test
    fun givenServerResponse200_whenFetch_shouldReturnSuccess() {
        val weatherDetails = Moshi.Builder().build().adapter(WeatherDetails::class.java).
            fromJson(getJson("success_resp.json"))
        val weatherLookupViewModel = WeatherLookupViewModel(weatherRepository, Dispatchers.Unconfined)
        coEvery { weatherRepository.getWeather(2.toDouble(), 3.toDouble())} returns weatherDetails!!
        weatherLookupViewModel.weatherDetails.observeForever{ }

        weatherLookupViewModel.lookup(2.toDouble(), 3.toDouble())

        assert(weatherLookupViewModel.weatherDetails.value != null)
        assert(weatherLookupViewModel.weatherDetails.value!!.name == weatherDetails.name)
        val testResult = weatherLookupViewModel.weatherDetails.value as WeatherDetails
        Assert.assertEquals(testResult.main.temp, "299.52")
    }

    @Test
    fun givenServerResponse400_whenFetch_shouldReturnError() {
        val errorMessage = "Error Message For You"
        val weatherLookupViewModel = WeatherLookupViewModel(weatherRepository, Dispatchers.Unconfined)
        coEvery { weatherRepository.getWeather(2.toDouble(), 3.toDouble())} throws RuntimeException(errorMessage)
        weatherLookupViewModel.showStatus.observeForever{ }

        weatherLookupViewModel.lookup(2.toDouble(), 3.toDouble())

        assert(weatherLookupViewModel.showStatus.value == errorMessage)
    }

}
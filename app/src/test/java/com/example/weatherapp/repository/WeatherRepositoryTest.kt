package com.example.weatherapp.repository

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.work.*
import com.example.weatherapp.base.BaseUTTest
import com.example.weatherapp.helper.WorkMangerHelper
import com.example.weatherapp.inerfaces.WeatherAPI
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockkObject
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import retrofit2.HttpException
import java.net.HttpURLConnection


@RunWith(JUnit4::class)
class WeatherRepositoryTest : BaseUTTest() {

    private lateinit var mRepo: WeatherRepository

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @MockK
    private lateinit var mContext: Context

    private lateinit var mWeatherAPI: WeatherAPI

    @MockK
    private lateinit var mWorkManager: WorkManager

    @MockK
    private lateinit var mPeriodicWorkRequest: PeriodicWorkRequest

    @Before
    fun start(){
        super.setUp()
        MockKAnnotations.init(this)
        mockkObject(WorkMangerHelper.Companion)
        mWeatherAPI = provideTestApiService()
        mRepo = WeatherRepository(mContext, mWeatherAPI, mWorkManager, mPeriodicWorkRequest)
    }

    @Test
    fun test_weather_repo_retrieves_expected_data() = runBlocking {

        mockNetworkResponseWithFileContent("success_resp.json", HttpURLConnection.HTTP_OK)
        every { WorkMangerHelper.Companion.getWorkStatus(mContext) } answers { true }

        val dataReceived = mRepo.getWeather(2.toDouble(), 3.toDouble())

        Assert.assertNotNull(dataReceived)
        Assert.assertEquals(dataReceived?.weather?.size, 1)
    }

    @Test
    fun test_weather_repo_retrieves_unexpected_data() = runBlocking {
        mockHttpResponse(503)
        every { WorkMangerHelper.Companion.getWorkStatus(mContext) } answers { true }

        val dataReceived = mRepo.getWeather(2.toDouble(), 3.toDouble())

        Assert.assertNull(dataReceived)
    }
}
package com.example.weatherapp.base

import com.example.weatherapp.inerfaces.WeatherAPI
import com.squareup.moshi.Moshi
import okhttp3.OkHttpClient
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.io.File

/**
 * Creates base
 */
abstract class BaseUTTest {

    /**
     * For MockWebServer instance
     */
    private lateinit var mMockServerInstance: MockWebServer

    /**
     * Default, let server be shut down
     */
    private var mShouldStart = false

    @Before
    open fun setUp(){
        startMockServer(true)
    }

    /**
     * Helps to read input file returns the respective data in mocked call
     */
    fun mockNetworkResponseWithFileContent(fileName: String, responseCode: Int) = mMockServerInstance.enqueue(
        MockResponse()
            .setResponseCode(responseCode)
            .setBody(getJson(fileName)))

    /**
     * Reads input file and converts to json
     */
    fun getJson(path : String) : String {
        val uri = javaClass.classLoader!!.getResource(path)
        val file = File(uri.path)
        return String(file.readBytes())
    }

    /**
     * Start Mockwebserver
     */
    private fun startMockServer(shouldStart:Boolean){
        if (shouldStart){
            mShouldStart = shouldStart
            mMockServerInstance = MockWebServer()
            mMockServerInstance.start()
        }
    }

    /**
     * Set Mockwebserver url
     */
    fun getMockWebServerUrl() = mMockServerInstance.url("/").toString()

    /**
     * Stop Mockwebserver
     */
    private fun stopMockServer() {
        if (mShouldStart){
            mMockServerInstance.shutdown()
        }
    }

    open fun mockHttpResponse(fileName: String, responseCode: Int) =
        mMockServerInstance.enqueue(MockResponse().setResponseCode(responseCode).setBody(getJson(fileName)))

    open fun mockHttpResponse(responseCode: Int) =
        mMockServerInstance.enqueue(MockResponse().setResponseCode(responseCode))

    fun provideTestApiService(): WeatherAPI {
        return Retrofit.Builder().baseUrl(mMockServerInstance.url("/")).addConverterFactory(
            MoshiConverterFactory.create(Moshi.Builder().build()))
            .client(OkHttpClient.Builder().build()).build().create(WeatherAPI::class.java)
    }

    @After
    open fun tearDown(){
        //Stop Mock server
        stopMockServer()
    }
}
package com.stevehechio.apps.magictheg.data.api

import com.stevehechio.apps.magictheg.data.remote.api.SetsApiService
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okio.buffer
import okio.source
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException
import java.nio.charset.StandardCharsets
import java.util.*

/**
 * Created by stevehechio on 11/30/21
 */
@RunWith(JUnit4::class)
open class SetsApiServiceTest {
    private lateinit var mockWebServer: MockWebServer
    private lateinit var setsApiService: SetsApiService
    @Before
    @Throws(IOException::class)
    fun mockServer() {
        mockWebServer = MockWebServer()
        mockWebServer.start()
       setsApiService = createService(SetsApiService::class.java)
    }

    private fun createService(clazz: Class<SetsApiService>): SetsApiService {
        return Retrofit.Builder()
            .baseUrl(mockWebServer.url("/"))
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
            .create(clazz)
    }

    @After
    @Throws(IOException::class)
    fun closeServer() {
        mockWebServer.shutdown()
    }

    @Test
    fun testToFetchSets() {
        val inputStream = Objects.requireNonNull(
            SetsApiServiceTest::class.java.classLoader
        )
            .getResourceAsStream(String.format("api-response/%s", "sets.json"))
        val source = inputStream.source().buffer()
        val mockResponse = MockResponse()
        mockWebServer.enqueue(mockResponse.setBody(source.readString(StandardCharsets.UTF_8)))
        val entity = setsApiService.fetchSetsForTest().blockingFirst().cardList[0]
        Assert.assertEquals("10E",entity.code)
    }
}

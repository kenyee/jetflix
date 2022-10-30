package com.yasinkacmaz.jetflix.testing.androidtest.mockweb

import android.content.Context
import androidx.test.platform.app.InstrumentationRegistry
import okhttp3.mockwebserver.Dispatcher
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.RecordedRequest
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader
import java.lang.IllegalArgumentException
import kotlin.reflect.KClass

class MockWebSuccessDispatcher(
    private val clazz: KClass<*>,
    private val responseFilesByPath: Map<String, String>,
    private val networkDelayMsec: Long = 200
) : Dispatcher() {
    private val context = InstrumentationRegistry.getInstrumentation().targetContext
    override fun dispatch(request: RecordedRequest): MockResponse {
        val requestPath = request.requestLine.split(' ')[1]
        val responseFile = responseFilesByPath.entries.firstOrNull {
            requestPath.startsWith(it.key)
        }?.value
        Thread.sleep(networkDelayMsec) // real network calls have a delay
        return if (responseFile != null) {
            val responseBody = asset(context, responseFile)
            MockResponse().setResponseCode(HTTP_STATUS_OK).setBody(responseBody)
        } else {
            throw IllegalArgumentException("No mock file for $requestPath")
        }
    }

    private fun asset(context: Context, assetPath: String): String {
        val rawAssetPath = "network_mocks/${clazz.simpleName}/$assetPath"
        try {
            val inputStream = context.assets.open(rawAssetPath)
            return inputStreamToString(inputStream)
        } catch (e: IOException) {
            throw IOException("Error reading asset file $rawAssetPath", e)
        }
    }

    private fun inputStreamToString(inputStream: InputStream, charsetName: String = "UTF-8"): String =
        InputStreamReader(inputStream, charsetName).readText()

    companion object {
        private const val HTTP_STATUS_OK = 200
    }
}

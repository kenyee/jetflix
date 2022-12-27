package com.yasinkacmaz.jetflix.testing.mockweb

import okhttp3.mockwebserver.Dispatcher
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.RecordedRequest
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader
import java.util.concurrent.TimeUnit

class MockWebSuccessDispatcher(
    private val responseFilesByPath: Map<String, String>,
    private val networkDelayMsec: Long = 200,
    private val assetAsInputStream: (String) -> InputStream
) : Dispatcher() {
    override fun dispatch(request: RecordedRequest): MockResponse {
        val requestPath = request.requestLine.split(' ')[1]
        val responseFile = responseFilesByPath.entries.firstOrNull {
            requestPath.startsWith(it.key)
        }?.value
        return if (responseFile != null) {
            val responseBody = asset(responseFile)
            MockResponse().setResponseCode(HTTP_STATUS_OK).setBody(responseBody)
                .setHeadersDelay(networkDelayMsec, TimeUnit.MILLISECONDS) // real network calls have a delay
        } else {
            throw IllegalArgumentException("No mock file for $requestPath")
        }
    }

    private fun asset(assetPath: String): String {
        try {
            val inputStream = assetAsInputStream(assetPath)
            return inputStreamToString(inputStream)
        } catch (e: IOException) {
            throw IOException("Error reading asset file $assetPath", e)
        }
    }

    private fun inputStreamToString(inputStream: InputStream, charsetName: String = "UTF-8"): String =
        InputStreamReader(inputStream, charsetName).readText()

    companion object {
        private const val HTTP_STATUS_OK = 200
    }
}

package com.yasinkacmaz.jetflix.testing.mockweb

import okhttp3.mockwebserver.Dispatcher
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.RecordedRequest
import java.util.concurrent.TimeUnit

class MockWebErrorDispatcher(
    private val httpErrorCode: Int,
    private val networkDelayMsec: Long = 200
) : Dispatcher() {
    override fun dispatch(request: RecordedRequest): MockResponse {
        return MockResponse().setResponseCode(httpErrorCode)
            .setHeadersDelay(networkDelayMsec, TimeUnit.MILLISECONDS) // real network calls have a delay
    }
}

package com.yasinkacmaz.jetflix.testing.androidtest.mockweb

import okhttp3.mockwebserver.Dispatcher
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.RecordedRequest

class MockWebErrorDispatcher(
    private val httpErrorCode: Int,
    private val networkDelayMsec: Long = 200
) : Dispatcher() {
    override fun dispatch(request: RecordedRequest): MockResponse {
        Thread.sleep(networkDelayMsec) // real network calls have a delay
        return MockResponse().setResponseCode(httpErrorCode)
    }
}

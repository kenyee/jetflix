package com.yasinkacmaz.jetflix.util.mockweb

import okhttp3.mockwebserver.Dispatcher
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.RecordedRequest

class MockWebErrorDispatcher(private val httpErrorCode: Int) : Dispatcher() {
    override fun dispatch(request: RecordedRequest): MockResponse {
        Thread.sleep(200) // real network calls have a delay
        return MockResponse().setResponseCode(httpErrorCode)
    }
}

package com.yasinkacmaz.jetflix.testing.mockweb

import okhttp3.mockwebserver.MockWebServer
import org.junit.rules.TestWatcher
import org.junit.runner.Description
import java.io.InputStream
import kotlin.reflect.KClass

open class MockWebServerRule(
    private val clazz: KClass<*>,
    private val port: Int
) : TestWatcher() {
    private val mockWebServer by lazy { MockWebServer() }

    override fun starting(description: Description) {
        mockWebServer.start(port)
        super.starting(description)
    }

    override fun finished(description: Description) {
        mockWebServer.shutdown()
        super.finished(description)
    }

    var errorCode: Int = 0
        set(value) {
            mockWebServer.dispatcher = MockWebErrorDispatcher(value)
        }

    var responseFiles: Map<String, String> = emptyMap()
        set(value) {
            mockWebServer.dispatcher = MockWebSuccessDispatcher(
                value,
                assetAsInputStream = this::assetAsInputStream
            )
        }

    open fun assetAsInputStream(path: String): InputStream =
        javaClass.classLoader?.getResourceAsStream(mapAssetPath(path))
            ?: error("Could not get inputstream for ${mapAssetPath(path)}")

    open fun mapAssetPath(assetPath: String): String =
        "network_mocks/${clazz.simpleName}/$assetPath"
}

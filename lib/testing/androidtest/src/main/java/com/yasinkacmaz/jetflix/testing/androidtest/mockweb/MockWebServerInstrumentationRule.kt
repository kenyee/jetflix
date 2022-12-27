package com.yasinkacmaz.jetflix.testing.androidtest.mockweb

import androidx.test.platform.app.InstrumentationRegistry
import com.yasinkacmaz.jetflix.testing.mockweb.MockWebServerRule
import java.io.InputStream
import kotlin.reflect.KClass

class MockWebServerInstrumentationRule(
    clazz: KClass<*>,
    port: Int
) : MockWebServerRule(clazz, port) {
    private val context = InstrumentationRegistry.getInstrumentation().targetContext

    override fun assetAsInputStream(path: String): InputStream =
        context.assets.open(mapAssetPath(path))
}

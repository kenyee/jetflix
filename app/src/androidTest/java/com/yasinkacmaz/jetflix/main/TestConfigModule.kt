package com.yasinkacmaz.jetflix.main

/* causes weird hilt "list is empty" compile error

import com.yasinkacmaz.jetflix.BuildConfig
import com.yasinkacmaz.jetflix.di.ConfigModule
import dagger.Module
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [ConfigModule::class]
)
open class TestConfigModule : ConfigModule() {
    override val baseUrl = "http://127.0.0.1:${BuildConfig.MOCKSERVER_PORT}"
}
*/

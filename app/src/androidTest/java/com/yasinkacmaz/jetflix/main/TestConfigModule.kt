/*
// Causes weird Room Hilt "list is empty" stacktrace
package com.yasinkacmaz.jetflix.main

import com.yasinkacmaz.jetflix.BuildConfig
import com.yasinkacmaz.jetflix.di.ConfigModule
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [ConfigModule::class]
)
open class TestConfigModule : ConfigModule() {
    override val baseUrl = "http://127.0.0.1:${BuildConfig.MOCKSERVER_PORT}"
}
*/

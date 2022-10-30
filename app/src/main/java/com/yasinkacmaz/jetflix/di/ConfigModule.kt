package com.yasinkacmaz.jetflix.di

import com.yasinkacmaz.jetflix.BuildConfig
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
open class ConfigModule {
    open val baseUrl = BuildConfig.API_BASE_URL

    @Provides
    @BaseUrl
    fun provideBaseUrl() = baseUrl
}

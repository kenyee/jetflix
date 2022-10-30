package com.yasinkacmaz.jetflix.di

import android.content.Context
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.yasinkacmaz.jetflix.BuildConfig
import com.yasinkacmaz.jetflix.R
import com.yasinkacmaz.jetflix.service.ConfigurationService
import com.yasinkacmaz.jetflix.service.MovieService
import com.yasinkacmaz.jetflix.ui.settings.LanguageDataStore
import com.yasinkacmaz.jetflix.util.interceptor.ApiKeyInterceptor
import com.yasinkacmaz.jetflix.util.interceptor.LanguageInterceptor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoSet
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    @IntoSet
    fun provideApiKeyInterceptor(@ApplicationContext context: Context): Interceptor {
        return ApiKeyInterceptor(context.getString(R.string.api_key))
    }

    @Provides
    @Singleton
    @IntoSet
    fun provideLanguageInterceptor(languageDataStore: LanguageDataStore): Interceptor {
        return LanguageInterceptor(languageDataStore)
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(
        interceptors: Set<@JvmSuppressWildcards Interceptor>
    ): OkHttpClient {
        return OkHttpClient.Builder().apply {
            interceptors().addAll(interceptors)
            if (BuildConfig.DEBUG) {
                val logger = HttpLoggingInterceptor()
                logger.setLevel(HttpLoggingInterceptor.Level.HEADERS)
                interceptors().add(logger)
            }
        }.build()
    }

    @OptIn(ExperimentalSerializationApi::class)
    @Provides
    @Singleton
    fun provideRetrofit(@BaseUrl baseUrl: String, okHttpClient: OkHttpClient, json: Json): Retrofit {
        return Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl(baseUrl)
            .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
            .build()
    }

    @Provides
    @Singleton
    fun provideMovieService(retrofit: Retrofit) = retrofit.create(MovieService::class.java)

    @Provides
    @Singleton
    fun provideConfigurationService(retrofit: Retrofit) = retrofit.create(ConfigurationService::class.java)
}

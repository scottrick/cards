package com.hatfat.cards.inject

import android.content.Context
import android.util.Log
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.Cache
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import java.io.File
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {
    @Provides
    @Singleton
    fun providesCache(
        @ApplicationContext context: Context
    ): Cache {
        /* 10 MB cache */
        return Cache(File(context.cacheDir, "http_cache"), 20 * 1024 * 1024)
    }

    @Provides
    @Singleton
    fun providesOkhttpClient(
        cache: Cache
    ): OkHttpClient {
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BASIC

        val cacheInterceptor = Interceptor { chain ->
            val response = chain.proceed(chain.request())
            if (response.networkResponse != null) {
                Log.i("OkhttpInfo", "Fetched from network: ${chain.request().url}")
            }
            response
        }

        return OkHttpClient.Builder()
            .addInterceptor(interceptor)
            .addInterceptor(cacheInterceptor)
            .connectTimeout(15, TimeUnit.SECONDS)
            .readTimeout(15, TimeUnit.SECONDS)
            .cache(cache)
            .build()
    }
}
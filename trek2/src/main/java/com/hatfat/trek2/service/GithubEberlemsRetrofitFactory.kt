package com.hatfat.trek2.service

import com.google.gson.Gson
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GithubEberlemsRetrofitFactory @Inject constructor(
    private val okHttpClient: OkHttpClient,
    private val gson: Gson
) {
    private val baseUrl = "https://raw.githubusercontent.com/eberlems/startrek2e/"

    val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }
}
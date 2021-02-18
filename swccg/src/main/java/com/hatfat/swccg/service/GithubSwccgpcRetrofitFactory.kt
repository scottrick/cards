package com.hatfat.swccg.service

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GithubSwccgpcRetrofitFactory @Inject constructor(
    private val okHttpClient: OkHttpClient
) {
    private val baseUrl = "https://raw.githubusercontent.com/swccgpc/swccg-card-json/"

    val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}
package com.hatfat.trek1.service

import okhttp3.ResponseBody
import retrofit2.http.GET

interface GithubEberlemsService {
    @GET("playable/sets/Physical.txt")
    suspend fun getPhysicalCards(): ResponseBody

    @GET("playable/sets/Virtual.txt")
    suspend fun getVirtualCards(): ResponseBody
}
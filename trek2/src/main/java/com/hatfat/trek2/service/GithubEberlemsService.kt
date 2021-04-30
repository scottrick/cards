package com.hatfat.trek2.service

import com.hatfat.trek2.data.Trek2Set
import okhttp3.ResponseBody
import retrofit2.http.GET

interface GithubEberlemsService {
    @GET("playable/sets/Physical.txt")
    suspend fun getPhysicalCards(): ResponseBody

    @GET("playable/sets/Virtual.txt")
    suspend fun getVirtualCards(): ResponseBody

    @GET("playable/sets/sets.json")
    suspend fun getSets(): List<Trek2Set>
}
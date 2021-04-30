package com.hatfat.trek1.service

import com.hatfat.trek1.data.Trek1Set
import okhttp3.ResponseBody
import retrofit2.http.GET

interface GithubEberlemsService {
    @GET("playable/sets/Physical.txt")
    suspend fun getPhysicalCards(): ResponseBody

    @GET("playable/sets/Virtual.txt")
    suspend fun getVirtualCards(): ResponseBody

    @GET("playable/sets/sets.json")
    suspend fun getSets(): List<Trek1Set>
}
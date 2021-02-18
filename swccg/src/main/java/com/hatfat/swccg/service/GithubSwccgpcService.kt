package com.hatfat.swccg.service

import com.hatfat.swccg.data.SWCCGCardList
import com.hatfat.swccg.data.SWCCGSet
import retrofit2.http.GET

interface GithubSwccgpcService {
    @GET("main/Dark.json")
    suspend fun getDarkSideJson(): SWCCGCardList

    @GET("main/Light.json")
    suspend fun getLightSideJson(): SWCCGCardList

    @GET("main/sets.json")
    suspend fun getSets(): List<SWCCGSet>
}
package com.hatfat.swccg.service

import com.hatfat.swccg.data.SWCCGCardList
import com.hatfat.swccg.data.SWCCGSet
import retrofit2.http.GET

interface GithubSwccgpcService {
    @GET("Dark.json")
    suspend fun getDarkSideJson(): SWCCGCardList

    @GET("Light.json")
    suspend fun getLightSideJson(): SWCCGCardList

    @GET("DarkLegacy.json")
    suspend fun getDarkSideLegacyJson(): SWCCGCardList

    @GET("LightLegacy.json")
    suspend fun getLightSideLegacyJson(): SWCCGCardList

    @GET("sets.json")
    suspend fun getSets(): List<SWCCGSet>
}
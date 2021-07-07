package com.hatfat.meccg.service

import com.hatfat.meccg.data.MECCGCard
import com.hatfat.meccg.data.MECCGSet
import retrofit2.http.GET

interface GithubCardnumService {
    @GET("master/fdata/cards-dc.json")
    suspend fun getCards(): List<MECCGCard>

    @GET("master/fdata/sets-dc.json")
    suspend fun getSets(): List<MECCGSet>
}
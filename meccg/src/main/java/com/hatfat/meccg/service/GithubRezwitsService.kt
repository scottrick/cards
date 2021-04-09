package com.hatfat.meccg.service

import com.hatfat.meccg.data.MECCGCard
import retrofit2.http.GET

interface GithubRezwitsService {
    @GET("master/fdata/cards-dc.json")
    suspend fun getCards(): List<MECCGCard>
}

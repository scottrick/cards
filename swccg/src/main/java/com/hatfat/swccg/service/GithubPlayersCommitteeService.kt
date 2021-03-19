package com.hatfat.swccg.service

import com.hatfat.swccg.data.format.SWCCGFormat
import retrofit2.http.GET

interface GithubPlayersCommitteeService {
    @GET("master/gemp-swccg-server/src/main/resources/swccgFormats.json")
    suspend fun getFormats(): List<SWCCGFormat>
}
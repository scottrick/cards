package com.hatfat.swccg.service

import com.hatfat.swccg.data.format.SWCCGFormat
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.GET

interface GithubPlayersCommitteeService {
    @GET("master/gemp-swccg-server/src/main/resources/swccgFormats.json")
    suspend fun getFormats(): List<SWCCGFormat>

    @GET("master/gemp-swccg-async/src/main/web/js/gemp-016/jCards.js")
    suspend fun getGempCards(): Response<ResponseBody>
}
package com.hatfat.swccg.inject

import com.hatfat.swccg.service.GithubPlayersCommitteeRetrofitFactory
import com.hatfat.swccg.service.GithubPlayersCommitteeService
import com.hatfat.swccg.service.GithubSwccgpcRetrofitFactory
import com.hatfat.swccg.service.GithubSwccgpcService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class SWCCGServicesModule {
    @Provides
    fun providesGithubPlayersCommitteeService(
        retrofitFactory: GithubPlayersCommitteeRetrofitFactory
    ): GithubPlayersCommitteeService {
        return retrofitFactory.retrofit.create(GithubPlayersCommitteeService::class.java)
    }

    @Provides
    fun providesGithubSwccgpcService(
        retrofitFactory: GithubSwccgpcRetrofitFactory
    ): GithubSwccgpcService {
        return retrofitFactory.retrofit.create(GithubSwccgpcService::class.java)
    }
}
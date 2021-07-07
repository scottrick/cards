package com.hatfat.meccg.inject

import com.hatfat.meccg.service.GithubCardnumRetrofitFactory
import com.hatfat.meccg.service.GithubCardnumService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class MECCGServicesModule {
    @Provides
    fun providesGithubCardnumService(
        retrofitFactory: GithubCardnumRetrofitFactory
    ): GithubCardnumService {
        return retrofitFactory.retrofit.create(GithubCardnumService::class.java)
    }
}
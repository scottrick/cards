package com.hatfat.meccg.inject

import com.hatfat.meccg.service.GithubRezwitsRetrofitFactory
import com.hatfat.meccg.service.GithubRezwitsService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class MECCGServicesModule {
    @Provides
    fun providesGithubRezwitsService(
        retrofitFactory: GithubRezwitsRetrofitFactory
    ): GithubRezwitsService {
        return retrofitFactory.retrofit.create(GithubRezwitsService::class.java)
    }
}
package com.hatfat.trek1.inject

import com.hatfat.trek1.service.GithubEberlemsRetrofitFactory
import com.hatfat.trek1.service.GithubEberlemsService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class Trek1ServicesModule {
    @Provides
    fun providesGithubEberlemsService(
        retrofitFactory: GithubEberlemsRetrofitFactory
    ): GithubEberlemsService {
        return retrofitFactory.retrofit.create(GithubEberlemsService::class.java)
    }
}
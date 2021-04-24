package com.hatfat.trek2.inject

import com.hatfat.trek2.service.GithubEberlemsRetrofitFactory
import com.hatfat.trek2.service.GithubEberlemsService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class Trek2ServicesModule {
    @Provides
    fun providesGithubEberlemsService(
        retrofitFactory: GithubEberlemsRetrofitFactory
    ): GithubEberlemsService {
        return retrofitFactory.retrofit.create(GithubEberlemsService::class.java)
    }
}
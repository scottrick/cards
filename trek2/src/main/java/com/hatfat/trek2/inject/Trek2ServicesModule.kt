package com.hatfat.trek2.inject

import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class Trek2ServicesModule {
//    @Provides
//    fun providesGithubRezwitsService(
//        retrofitFactory: GithubRezwitsRetrofitFactory
//    ): GithubRezwitsService {
//        return retrofitFactory.retrofit.create(GithubRezwitsService::class.java)
//    }
}
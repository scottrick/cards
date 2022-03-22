package com.hatfat.swccg.inject

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class SWCCGEndpointsModule {
    @Provides
    @Singleton
    @Named("github_base_url")
    fun providesGithubBaseUrl(): String {
//        return "https://raw.githubusercontent.com/scottrick/swccg-card-json/html-rulings/"
        return "https://raw.githubusercontent.com/swccgpc/swccg-card-json/main/"
    }
}
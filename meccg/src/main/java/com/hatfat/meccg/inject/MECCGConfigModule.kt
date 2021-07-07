package com.hatfat.meccg.inject

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object MECCGConfigModule {
    @Provides
    @Singleton
    @Named("should use dreamcards")
    fun providesShouldUseDreamCards(): Boolean {
        return false
    }
}

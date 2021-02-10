package com.hatfat.swccg.inject

import com.hatfat.cards.InterfaceForTesting
import com.hatfat.swccg.TempClass
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object SWCCGModule {

    @Provides
    fun provideTempClass(): InterfaceForTesting {
        return TempClass()
    }
}

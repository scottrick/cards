package com.hatfat.meccg.inject

import com.hatfat.cards.InterfaceForTesting
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent


@Module
@InstallIn(SingletonComponent::class)
object MECCGModule {

    @Provides
    fun provideTempClass(): InterfaceForTesting {
        return object : InterfaceForTesting {
            override fun testString(): String {
                return "MECCG IMPLEMENTED String."
            }
        }
    }
}

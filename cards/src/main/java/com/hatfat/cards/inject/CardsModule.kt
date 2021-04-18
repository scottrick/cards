package com.hatfat.cards.inject

import android.content.Context
import android.content.res.Resources
import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import java.util.*
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object CardsModule {
    @Provides
    fun providesLocalBroadcastManager(@ApplicationContext context: Context): Resources {
        return context.resources
    }

    @Provides
    @Singleton
    fun providesGson(): Gson {
        return Gson()
    }

    @Provides
    @Singleton
    fun providesRandom(): Random {
        return Random(Date().time)
    }

    @Provides
    @Singleton
    @Named("should use playstore images")
    fun providesShouldUsePlaystoreImages(): Boolean {
        return false
    }
}
package com.hatfat.cards.app

import android.content.Context
import android.content.res.Resources
import android.util.Log
import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import java.util.*
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
        Log.e("catfat", "CREATING NEW GSON")
        return Gson()
    }

    @Provides
    @Singleton
    fun providesRandom(): Random {
        return Random(Date().time)
    }
}
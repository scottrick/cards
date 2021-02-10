package com.hatfat.meccg

import android.util.Log
import com.hatfat.cards.CardsApplication
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MECCGApplication : CardsApplication() {
    override fun onCreate() {
        super.onCreate()
        Log.e("catfat", "SWCCG onCreate!")
    }
}

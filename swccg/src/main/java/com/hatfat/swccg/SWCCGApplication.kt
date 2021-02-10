package com.hatfat.swccg

import android.util.Log
import com.hatfat.cards.CardsApplication
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class SWCCGApplication : CardsApplication() {
    override fun onCreate() {
        super.onCreate()
        Log.e("catfat", "SWCCG onCreate!")
    }
}

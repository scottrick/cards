package com.hatfat.meccg

import com.hatfat.cards.app.CardsApplication
import com.hatfat.meccg.repo.MECCGCardsRepository
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class MECCGApplication : CardsApplication() {
    @Inject
    lateinit var cardRepository: MECCGCardsRepository
}

package com.hatfat.swccg

import com.hatfat.cards.app.CardsApplication
import com.hatfat.swccg.repo.SWCCGCardRepository
import com.hatfat.swccg.repo.SWCCGFormatRepository
import com.hatfat.swccg.repo.SWCCGSetRepository
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class SWCCGApplication : CardsApplication() {
    @Inject
    lateinit var cardRepository: SWCCGCardRepository

    @Inject
    lateinit var formatRepository: SWCCGFormatRepository

    @Inject
    lateinit var setRepository: SWCCGSetRepository
}

package com.hatfat.swccg

import com.hatfat.cards.app.CardsApplication
import com.hatfat.swccg.repo.SWCCGMetaDataRepository
import com.hatfat.swccg.repo.SWCCGCardsRepository
import com.hatfat.swccg.repo.SWCCGFormatRepository
import com.hatfat.swccg.repo.SWCCGSetRepository
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class SWCCGApplication : CardsApplication() {
    @Inject
    lateinit var cardRepository: SWCCGCardsRepository

    @Inject
    lateinit var formatRepository: SWCCGFormatRepository

    @Inject
    lateinit var setRepository: SWCCGSetRepository

    @Inject
    lateinit var metaDataRepository: SWCCGMetaDataRepository
}

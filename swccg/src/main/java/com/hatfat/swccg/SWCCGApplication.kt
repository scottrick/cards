package com.hatfat.swccg

import com.hatfat.cards.app.CardsApplication
import com.hatfat.cards.results.SearchResultsRepository
import com.hatfat.swccg.repo.SWCCGCardRepository
import com.hatfat.swccg.repo.SWCCGFormatRepository
import com.hatfat.swccg.repo.SWCCGMetaDataRepository
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

    @Inject
    lateinit var metaDataRepository: SWCCGMetaDataRepository

    @Inject
    lateinit var searchResultsRepository: SearchResultsRepository

    override fun onCreate() {
        super.onCreate()

        cardRepository.prepare()
        formatRepository.prepare()
        setRepository.prepare()
        metaDataRepository.prepare()
        searchResultsRepository.prepare()
    }
}

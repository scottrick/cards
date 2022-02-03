package com.hatfat.meccg

import com.hatfat.cards.app.CardsApplication
import com.hatfat.meccg.repo.MECCGCardRepository
import com.hatfat.meccg.repo.MECCGMetaDataRepository
import com.hatfat.meccg.repo.MECCGSetRepository
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class MECCGApplication : CardsApplication() {
    @Inject
    lateinit var cardRepository: MECCGCardRepository

    @Inject
    lateinit var setRepository: MECCGSetRepository

    @Inject
    lateinit var metaDataRepository: MECCGMetaDataRepository

    override fun onCreate() {
        super.onCreate()

        cardRepository.prepare()
        setRepository.prepare()
        metaDataRepository.prepare()
    }
}
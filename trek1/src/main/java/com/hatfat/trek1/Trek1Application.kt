package com.hatfat.trek1

import com.hatfat.cards.app.CardsApplication
import com.hatfat.trek1.repo.Trek1CardRepository
import com.hatfat.trek1.repo.Trek1MetaDataRepository
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class Trek1Application : CardsApplication() {
    @Inject
    lateinit var cardRepository: Trek1CardRepository

    @Inject
    lateinit var metaDataRepository: Trek1MetaDataRepository
}
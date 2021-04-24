package com.hatfat.trek2

import com.hatfat.cards.app.CardsApplication
import com.hatfat.trek2.repo.Trek2CardRepository
import com.hatfat.trek2.repo.Trek2MetaDataRepository
import com.hatfat.trek2.repo.Trek2SetRepository
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class Trek2Application : CardsApplication() {
    @Inject
    lateinit var cardRepository: Trek2CardRepository

    @Inject
    lateinit var setRepository: Trek2SetRepository

    @Inject
    lateinit var metaDataRepository: Trek2MetaDataRepository
}
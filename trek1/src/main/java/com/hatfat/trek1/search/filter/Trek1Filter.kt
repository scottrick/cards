package com.hatfat.trek1.search.filter

import com.hatfat.trek1.data.Trek1Card
import com.hatfat.trek1.repo.Trek1MetaDataRepository
import com.hatfat.trek1.repo.Trek1SetRepository

interface Trek1Filter {
    fun filter(card: Trek1Card, setRepository: Trek1SetRepository, metaDataRepository: Trek1MetaDataRepository): Boolean
}
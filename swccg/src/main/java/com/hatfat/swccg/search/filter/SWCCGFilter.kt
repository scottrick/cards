package com.hatfat.swccg.search.filter

import com.hatfat.swccg.data.SWCCGCard
import com.hatfat.swccg.repo.SWCCGSetRepository

interface SWCCGFilter {
    fun filter(card: SWCCGCard, setRepository: SWCCGSetRepository): Boolean
}
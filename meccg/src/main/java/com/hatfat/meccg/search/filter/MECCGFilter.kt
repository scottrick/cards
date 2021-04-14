package com.hatfat.meccg.search.filter

import com.hatfat.meccg.data.MECCGCard
import com.hatfat.meccg.repo.MECCGSetRepository

interface MECCGFilter {
    fun filter(card: MECCGCard, setRepository: MECCGSetRepository): Boolean
}
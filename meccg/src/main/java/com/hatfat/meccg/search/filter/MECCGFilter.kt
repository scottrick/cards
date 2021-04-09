package com.hatfat.meccg.search.filter

import com.hatfat.meccg.data.MECCGCard

interface MECCGFilter {
    fun filter(card: MECCGCard): Boolean
}
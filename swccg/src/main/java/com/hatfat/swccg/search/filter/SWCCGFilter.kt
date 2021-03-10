package com.hatfat.swccg.search.filter

import com.hatfat.swccg.data.SWCCGCard

interface SWCCGFilter {
    fun filter(card: SWCCGCard): Boolean
}
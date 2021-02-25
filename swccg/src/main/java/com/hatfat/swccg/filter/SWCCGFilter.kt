package com.hatfat.swccg.filter

import com.hatfat.swccg.data.SWCCGCard

abstract class SWCCGFilter {
    abstract fun filter(card: SWCCGCard): Boolean
}
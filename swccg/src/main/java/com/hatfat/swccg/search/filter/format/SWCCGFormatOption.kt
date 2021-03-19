package com.hatfat.swccg.search.filter.format

import com.hatfat.cards.search.filter.SpinnerOption
import com.hatfat.swccg.data.format.SWCCGFormat

data class SWCCGFormatOption(
    val format: SWCCGFormat
) : SpinnerOption {
    override val displayName: String
        get() = format.name
}
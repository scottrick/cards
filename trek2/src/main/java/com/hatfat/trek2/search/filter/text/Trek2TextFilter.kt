package com.hatfat.trek2.search.filter.text

import com.hatfat.trek2.data.Trek2Card
import com.hatfat.trek2.search.filter.Trek2Filter

class Trek2TextFilter(
    private val filterText: String,
    private val textFilterModes: Set<Trek2TextFilterMode>
) : Trek2Filter {
    override fun filter(card: Trek2Card): Boolean {
//        if (textFilterModes.contains(Trek2TextFilterMode.TITLE) &&
//            (card.normalizedTitle?.contains(filterText, true) == true
//                    || card.nameEN?.contains(filterText, true) == true)
//        ) {
//            return true
//        }
//
//        if (textFilterModes.contains(Trek2TextFilterMode.TEXT)
//            && card.text?.contains(filterText, true) == true
//        ) {
//            return true
//        }
//
//        return false
        return true
    }
}
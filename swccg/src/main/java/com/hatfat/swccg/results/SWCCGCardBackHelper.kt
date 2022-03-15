package com.hatfat.swccg.results

import androidx.annotation.DrawableRes
import com.hatfat.swccg.R
import com.hatfat.swccg.data.SWCCGCard

class SWCCGCardBackHelper {
    @DrawableRes
    fun getCardBackResourceId(card: SWCCGCard?): Int {
        if (card?.side?.compareTo("light", true) == 0) {
            /* light side */
            return R.drawable.light_card_back
        } else {
            /* otherwise just assume dark side */
            return R.drawable.dark_card_back
        }
    }
}
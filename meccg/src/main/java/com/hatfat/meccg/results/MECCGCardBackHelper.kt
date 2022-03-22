package com.hatfat.meccg.results

import androidx.annotation.DrawableRes
import com.hatfat.meccg.R
import com.hatfat.meccg.data.MECCGCard

class MECCGCardBackHelper {
    @DrawableRes
    fun getCardBackResourceId(card: MECCGCard?): Int {
        return if (card?.primary?.compareTo("region", true) == 0) {
            /* region card */
            R.drawable.flip
        } else {
            /* otherwise just assume lidless eye cardback */
            R.drawable.lidless_eye_cardback
        }
    }
}

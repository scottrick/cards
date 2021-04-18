package com.hatfat.trek1.search.filter.set

import com.hatfat.cards.search.filter.SpinnerFilter
import com.hatfat.trek1.data.Trek1Card
import com.hatfat.trek1.search.filter.Trek1Filter
import com.hatfat.trek1.search.filter.set.Trek1SetOption
import java.io.Serializable

class Trek1SetFilter(
    options: List<Trek1SetOption>,
    notSelectedOption: Trek1SetOption?
) : SpinnerFilter(
    options,
    notSelectedOption
), Trek1Filter, Serializable {
    override fun filter(card: Trek1Card): Boolean {
        return true
//        return (selectedOption as Trek1SetOption).code == card.set
    }
}
package com.hatfat.meccg.search.filter.dreamcard

import com.hatfat.cards.search.filter.SpinnerFilter
import com.hatfat.meccg.data.MECCGCard
import com.hatfat.meccg.repo.MECCGSetRepository
import com.hatfat.meccg.search.filter.MECCGFilter
import java.io.Serializable

class MECCGDreamcardFilter(
    options: List<MECCGDreamcardOption>,
    notSelectedOption: MECCGDreamcardOption?
) : SpinnerFilter(
    options,
    notSelectedOption
), MECCGFilter, Serializable {
    override fun filter(card: MECCGCard, setRepository: MECCGSetRepository): Boolean {
        return when ((selectedOption as MECCGDreamcardOption).mode) {
            MECCGDreamcardOptionMode.ALL_CARDS -> true
            MECCGDreamcardOptionMode.NO_DREAMCARDS -> card.dreamcard == false
            MECCGDreamcardOptionMode.ONLY_DREAMCARDS -> card.dreamcard == true
        }
    }
}
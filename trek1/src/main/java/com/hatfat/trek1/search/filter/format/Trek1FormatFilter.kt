package com.hatfat.trek1.search.filter.format

import com.hatfat.cards.search.filter.SpinnerFilter
import com.hatfat.trek1.data.Trek1Card
import com.hatfat.trek1.repo.Trek1MetaDataRepository
import com.hatfat.trek1.repo.Trek1SetRepository
import com.hatfat.trek1.search.filter.Trek1Filter
import java.io.Serializable

class Trek1FormatFilter(
    options: List<Trek1FormatOption>,
    notSelectedOption: Trek1FormatOption?
) : SpinnerFilter(
    options,
    notSelectedOption
), Trek1Filter, Serializable {
    override fun filter(card: Trek1Card, setRepository: Trek1SetRepository, metaDataRepository: Trek1MetaDataRepository): Boolean {
        return when ((selectedOption as Trek1FormatOption).optionType) {
            Trek1FormatOptionType.ANY -> true
            Trek1FormatOptionType.NO_VIRTUAL -> {
                !(setRepository.setMap.value?.get(card.release)?.virtual ?: false)
            }
            Trek1FormatOptionType.ONLY_VIRTUAL -> {
                (setRepository.setMap.value?.get(card.release)?.virtual ?: false)
            }
            Trek1FormatOptionType.PAQ -> {
                card.release == "premiere" || card.release == "au" || card.release == "qc"
            }
        }
    }
}
package com.hatfat.trek1.search.filter.format

import com.hatfat.cards.search.filter.SpinnerFilter
import com.hatfat.trek1.data.Trek1Card
import com.hatfat.trek1.repo.Trek1MetaDataRepository
import com.hatfat.trek1.repo.Trek1SetRepository
import com.hatfat.trek1.search.filter.Trek1Filter
import java.io.Serializable

class Trek1FormatFilter(
    options: List<Trek1FormatOption>,
    notSelectedOption: Trek1FormatOption?,
    defaultOption: Trek1FormatOption?,
) : SpinnerFilter(
    options,
    notSelectedOption,
    defaultOption,
    defaultOption
), Trek1Filter, Serializable {
    override fun filter(card: Trek1Card, setRepository: Trek1SetRepository, metaDataRepository: Trek1MetaDataRepository): Boolean {
        return when ((selectedOption as Trek1FormatOption).optionType) {
            Trek1FormatOptionType.ANY -> {
                !card.is2eCompatible
            }
            Trek1FormatOptionType.NO_VIRTUAL -> {
                !card.is2eCompatible && !(setRepository.setMap.value?.get(card.release)?.virtual ?: false)
            }
            Trek1FormatOptionType.ONLY_VIRTUAL -> {
                !card.is2eCompatible && (setRepository.setMap.value?.get(card.release)?.virtual ?: false)
            }
            Trek1FormatOptionType.PAQ -> {
                !card.is2eCompatible && card.release == "premiere" || card.release == "au" || card.release == "qc"
            }
            Trek1FormatOptionType.INCLUDING_2E_COMPAT -> {
                return true
            }
            Trek1FormatOptionType.ONLY_2E_COMPAT -> {
                card.is2eCompatible
            }
        }
    }
}
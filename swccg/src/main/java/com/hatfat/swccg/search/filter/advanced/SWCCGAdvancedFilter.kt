package com.hatfat.swccg.search.filter.advanced

import com.hatfat.cards.search.filter.advanced.AdvancedFilter
import com.hatfat.cards.search.filter.advanced.AdvancedFilterMode
import com.hatfat.swccg.data.SWCCGCard
import com.hatfat.swccg.repo.SWCCGSetRepository
import com.hatfat.swccg.search.filter.SWCCGFilter
import java.io.Serializable

class SWCCGAdvancedFilter(
    override val fields: List<SWCCGAdvancedFilterField>,
    override val modes: List<AdvancedFilterMode>
) : AdvancedFilter(
    fields,
    modes
), SWCCGFilter, Serializable {
    override fun filter(card: SWCCGCard, setRepository: SWCCGSetRepository): Boolean {
        val field = fields[selectedFieldIndex]
        field.getFieldValuesForCard(card, setRepository).forEach {
            if (fieldFilter(it)) {
                return true
            }
        }
        return false
    }
}
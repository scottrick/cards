package com.hatfat.meccg.search.filter.advanced

import com.hatfat.cards.search.filter.advanced.AdvancedFilter
import com.hatfat.cards.search.filter.advanced.AdvancedFilterMode
import com.hatfat.meccg.data.MECCGCard
import com.hatfat.meccg.repo.MECCGSetRepository
import com.hatfat.meccg.search.filter.MECCGFilter
import java.io.Serializable

class MECCGAdvancedFilter(
    override val fields: List<MECCGAdvancedFilterField>,
    override val modes: List<AdvancedFilterMode>
) : AdvancedFilter(
    fields,
    modes
), MECCGFilter, Serializable {
    override fun filter(card: MECCGCard, setRepository: MECCGSetRepository): Boolean {
        val field = fields[selectedFieldIndex]
        val fieldValues = field.getFieldValuesForCard(card, setRepository)

        if (fieldValues.isEmpty()) {
            return false
        }

        return fieldsFilter(fieldValues)
    }
}
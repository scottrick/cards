package com.hatfat.trek1.search.filter.advanced

import com.hatfat.cards.search.filter.advanced.AdvancedFilter
import com.hatfat.cards.search.filter.advanced.AdvancedFilterMode
import com.hatfat.trek1.data.Trek1Card
import com.hatfat.trek1.repo.Trek1MetaDataRepository
import com.hatfat.trek1.repo.Trek1SetRepository
import com.hatfat.trek1.search.filter.Trek1Filter
import java.io.Serializable

class Trek1AdvancedFilter(
    override val fields: List<Trek1AdvancedFilterField>,
    override val modes: List<AdvancedFilterMode>
) : AdvancedFilter(
    fields,
    modes
), Trek1Filter, Serializable {
    override fun filter(
        card: Trek1Card,
        setRepository: Trek1SetRepository,
        metaDataRepository: Trek1MetaDataRepository
    ): Boolean {
        val field = fields[selectedFieldIndex]
        val fieldValues = field.getFieldValuesForCard(card, setRepository, metaDataRepository)

        if (fieldValues.isEmpty()) {
            return false
        }

        return fieldsFilter(fieldValues)
    }
}
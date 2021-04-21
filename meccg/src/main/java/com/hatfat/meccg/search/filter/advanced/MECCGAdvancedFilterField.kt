package com.hatfat.meccg.search.filter.advanced

import com.hatfat.cards.search.filter.advanced.AdvancedFilterField
import com.hatfat.meccg.data.MECCGCard
import com.hatfat.meccg.repo.MECCGSetRepository
import java.io.Serializable

data class MECCGAdvancedFilterField(
    val meccgField: MECCGField
) : AdvancedFilterField, Serializable {

    override val displayName: String
        get() = meccgField.displayName

    fun getFieldValuesForCard(card: MECCGCard, setRepository: MECCGSetRepository): List<String> {
        return mutableListOf<String>().apply {
            when (meccgField) {
                MECCGField.ALIGNMENT -> card.alignment
                MECCGField.SET -> card.set
                MECCGField.PRIMARY -> card.primary
                MECCGField.ARTIST -> card.artist
                MECCGField.TEXT -> card.text
                MECCGField.SKILL -> card.skill
                MECCGField.MP -> card.mp
                MECCGField.MIND -> card.mind.toString()
                MECCGField.DIRECT -> card.direct.toString()
                MECCGField.GENERAL -> card.general.toString()
                MECCGField.PROWESS -> card.prowess.toString()
                MECCGField.BODY -> card.body.toString()
//                MECCGField.CORRUPTION -> card.corruption.toString()
                MECCGField.HOME -> card.home
                MECCGField.SECONDARY -> card.secondary
                MECCGField.RACE -> card.race
                MECCGField.SITE -> card.site
                MECCGField.PATH -> card.path
                MECCGField.REGION -> card.region
                MECCGField.HAVEN -> card.haven
                MECCGField.STAGE -> card.stage.toString()
                MECCGField.STRIKES -> card.strikes.toString()
                else -> null /* should be handled below */
            }?.takeIf { it.isNotBlank() }?.let {
                this.add(it)
            }

            when (meccgField) {
                MECCGField.RARITY -> {
                    card.rarity?.let {
                        this.add(it)
                    }
                    card.precise?.let {
                        this.add(it)
                    }
                }
                MECCGField.SET -> {
                    card.set?.let { setKey ->
                        this.add(setKey)

                        val set = setRepository.setMap.value?.get(setKey)
                        set?.name?.let {
                            this.add(it)
                        }
                    }
                }
                MECCGField.TITLE -> {
                    card.normalizedTitle?.let { this.add(it) }
                    card.nameEN?.let { this.add(it) }
                }
                else -> {
                    /* should be handled above */
                }
            }
        }
    }
}
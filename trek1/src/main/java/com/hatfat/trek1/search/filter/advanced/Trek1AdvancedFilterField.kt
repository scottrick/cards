package com.hatfat.trek1.search.filter.advanced

import com.hatfat.cards.search.filter.advanced.AdvancedFilterField
import com.hatfat.trek1.data.Trek1Card
import com.hatfat.trek1.repo.Trek1SetRepository
import java.io.Serializable

data class Trek1AdvancedFilterField(
    val trek1Field: Trek1Field
) : AdvancedFilterField, Serializable {

    override val displayName: String
        get() = trek1Field.displayName

    fun getFieldValuesForCard(card: Trek1Card, setRepository: Trek1SetRepository): List<String> {
        return mutableListOf<String>().apply {
            when (trek1Field) {
                Trek1Field.NAME -> card.name
                Trek1Field.PROPERTY -> card.property
                Trek1Field.UNIQUENESS -> card.uniqueness
                Trek1Field.TYPE -> card.type
                Trek1Field.MISSION_TYPE -> {
                    if (card.type == "Mission") {
                        card.missionDilemmaType
                    } else {
                        null
                    }
                }
                Trek1Field.DILEMMA_TYPE -> {
                    if (card.type == "Dilemma") {
                        card.missionDilemmaType
                    } else {
                        null
                    }
                }
                Trek1Field.AFFILIATION -> card.affiliation
                Trek1Field.CLASS -> card.cardClass
                Trek1Field.INTEGRITY -> {
                    if (card.type == "Personnel") {
                        card.intRng
                    } else {
                        null
                    }
                }
                Trek1Field.CUNNING -> {
                    if (card.type == "Personnel") {
                        card.cunWpn
                    } else {
                        null
                    }
                }
                Trek1Field.STRENGTH -> {
                    if (card.type == "Personnel") {
                        card.strShd
                    } else {
                        null
                    }
                }
                Trek1Field.RANGE -> {
                    if (card.type == "Ship") {
                        card.intRng
                    } else {
                        null
                    }
                }
                Trek1Field.WEAPONS -> {
                    if (card.type == "Ship") {
                        card.cunWpn
                    } else {
                        null
                    }
                }
                Trek1Field.SHIELDS -> {
                    if (card.type == "Ship") {
                        card.strShd
                    } else {
                        null
                    }
                }
                Trek1Field.POINTS -> card.points
                Trek1Field.REGION -> card.region
                Trek1Field.QUADRANT -> card.quadrant
                Trek1Field.SPAN -> card.span
                Trek1Field.ICONS -> card.icons
                Trek1Field.STAFF -> card.staff
                Trek1Field.CHARACTERISTICS -> card.characteristicsKeywords
                Trek1Field.REQUIRES -> card.requires
                Trek1Field.PERSONA -> card.persona
                Trek1Field.COMMAND -> card.command
                Trek1Field.LORE -> card.lore
                Trek1Field.REPORTS -> card.reports
                Trek1Field.NAMES -> card.names
                Trek1Field.GAMETEXT -> card.text
                else -> null /* should be handled below */
            }?.takeIf { it.isNotBlank() }?.let {
                this.add(it)
            }

            when (trek1Field) {
                Trek1Field.SET -> {
                    card.release?.let { setCode ->
                        this.add(setCode)

                        val set = setRepository.setMap.value?.get(setCode)
                        set?.name?.let {
                            this.add(it)
                        }
                    }
                }
                else -> {
                    /* should be handled above */
                }
            }
        }
    }
}
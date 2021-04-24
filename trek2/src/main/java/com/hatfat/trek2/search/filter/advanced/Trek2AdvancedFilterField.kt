package com.hatfat.trek2.search.filter.advanced

import com.hatfat.cards.search.filter.advanced.AdvancedFilterField
import com.hatfat.trek2.data.Trek2Card
import com.hatfat.trek2.repo.Trek2SetRepository
import java.io.Serializable

data class Trek2AdvancedFilterField(
    val trek2Field: Trek2Field
) : AdvancedFilterField, Serializable {

    override val displayName: String
        get() = trek2Field.displayName

    fun getFieldValuesForCard(card: Trek2Card, setRepository: Trek2SetRepository): List<String> {
        return mutableListOf<String>().apply {
            when (trek2Field) {
                Trek2Field.NAME -> card.name
                Trek2Field.TYPE -> card.type
                Trek2Field.MISSION_TYPE -> {
                    if (card.type == "Mission") {
                        card.missionDilemmaType
                    } else {
                        null
                    }
                }
                Trek2Field.DILEMMA_TYPE -> {
                    if (card.type == "Dilemma") {
                        card.missionDilemmaType
                    } else {
                        null
                    }
                }
                Trek2Field.AFFILIATION -> card.affiliation
                Trek2Field.CLASS -> card.cardClass
                Trek2Field.INTEGRITY -> {
                    if (card.type == "Personnel") {
                        card.intRng
                    } else {
                        null
                    }
                }
                Trek2Field.CUNNING -> {
                    if (card.type == "Personnel") {
                        card.cunWpn
                    } else {
                        null
                    }
                }
                Trek2Field.STRENGTH -> {
                    if (card.type == "Personnel") {
                        card.strShd
                    } else {
                        null
                    }
                }
                Trek2Field.RANGE -> {
                    if (card.type == "Ship") {
                        card.intRng
                    } else {
                        null
                    }
                }
                Trek2Field.WEAPONS -> {
                    if (card.type == "Ship") {
                        card.cunWpn
                    } else {
                        null
                    }
                }
                Trek2Field.SHIELDS -> {
                    if (card.type == "Ship") {
                        card.strShd
                    } else {
                        null
                    }
                }
                Trek2Field.POINTS -> card.points
                Trek2Field.QUADRANT -> card.quadrant
                Trek2Field.SPAN -> card.span
                Trek2Field.ICONS -> card.icons
                Trek2Field.STAFF -> card.staff
                Trek2Field.RARITY -> card.rarity
                Trek2Field.UNIQUE -> card.unique
                Trek2Field.COLLECTORS_INFO -> card.collectorsInfo
                Trek2Field.COST -> card.cost
                Trek2Field.KEYWORDS -> card.keywords
                Trek2Field.SPECIES -> card.species
                Trek2Field.SKILLS -> card.skills
                Trek2Field.TEXT -> card.text
                else -> null /* should be handled below */
            }?.takeIf { it.isNotBlank() }?.let {
                this.add(it)
            }

            when (trek2Field) {
                Trek2Field.SET -> {
                    card.set?.let { setCode ->
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
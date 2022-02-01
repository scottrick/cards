package com.hatfat.swccg.search.filter.advanced

import com.hatfat.cards.search.filter.advanced.AdvancedFilterField
import com.hatfat.swccg.data.SWCCGCard
import com.hatfat.swccg.repo.SWCCGSetRepository
import java.io.Serializable

data class SWCCGAdvancedFilterField(
    val swccgField: SWCCGField
) : AdvancedFilterField, Serializable {

    override val displayName: String
        get() = swccgField.displayName

    fun getFieldValuesForCard(card: SWCCGCard, setRepository: SWCCGSetRepository): List<String> {
        return mutableListOf<String>().apply {
            when (swccgField) {
                SWCCGField.GAMETEXT -> {
                    card.front.gametext?.let { this.add(it) }
                    card.back?.gametext?.let { this.add(it) }
                }
                SWCCGField.FORFEIT -> {
                    card.front.forfeit?.let { this.add(it) }
                    card.back?.forfeit?.let { this.add(it) }
                }
                SWCCGField.ABILITY -> {
                    card.front.ability?.let { this.add(it) }
                    card.back?.ability?.let { this.add(it) }
                }
                SWCCGField.ARMOR -> {
                    card.front.armor?.let { this.add(it) }
                    card.back?.armor?.let { this.add(it) }
                }
                SWCCGField.CHARACTERISTIC -> {
                    card.front.characteristics?.forEach { this.add(it) }
                    card.back?.characteristics?.forEach { this.add(it) }
                }
                SWCCGField.CONCEPT_BY -> {
                    card.conceptBy?.let { this.add(it) }
                }
                SWCCGField.DARK_SIDE_ICONS -> {
                    card.front.darkSideIcons?.let { this.add(it.toString()) }
                    card.back?.darkSideIcons?.let { this.add(it.toString()) }
                }
                SWCCGField.DEPLOY -> {
                    card.front.deploy?.let { this.add(it) }
                    card.back?.deploy?.let { this.add(it) }
                }
                SWCCGField.DESTINY -> {
                    card.front.destiny?.let { this.add(it) }
                    card.front.destinyValues?.forEach { this.add(it.toString()) }
                    card.back?.destiny?.let { this.add(it) }
                    card.back?.destinyValues?.forEach { this.add(it.toString()) }
                }
                SWCCGField.EXTRA_TEXT -> {
                    card.front.extraText?.forEach { this.add(it) }
                    card.back?.extraText?.forEach { this.add(it) }
                }
                SWCCGField.HYPERSPEED -> {
                    card.front.hyperspeed?.let { this.add(it) }
                    card.back?.hyperspeed?.let { this.add(it) }
                }
                SWCCGField.ICONS -> {
                    card.front.icons?.forEach { this.add(it) }
                    card.back?.icons?.forEach { this.add(it) }
                }
                SWCCGField.LANDSPEED -> {
                    card.front.landspeed?.let { this.add(it) }
                    card.back?.landspeed?.let { this.add(it) }
                }
                SWCCGField.LIGHT_SIDE_ICONS -> {
                    card.front.lightSideIcons?.let { this.add(it.toString()) }
                    card.back?.lightSideIcons?.let { this.add(it.toString()) }
                }
                SWCCGField.LORE -> {
                    card.front.lore?.let { this.add(it) }
                    card.back?.lore?.let { this.add(it) }
                }
                SWCCGField.MANEUVER -> {
                    card.front.maneuver?.let { this.add(it) }
                    card.back?.maneuver?.let { this.add(it) }
                }
                SWCCGField.PARSEC -> {
                    card.front.parsec?.let { this.add(it) }
                    card.back?.parsec?.let { this.add(it) }
                }
                SWCCGField.POLITICS -> {
                    card.front.politics?.let { this.add(it) }
                    card.back?.politics?.let { this.add(it) }
                }
                SWCCGField.POWER -> {
                    card.front.power?.let { this.add(it) }
                    card.back?.power?.let { this.add(it) }
                }
                SWCCGField.RARITY -> {
                    card.rarity?.let { this.add(it) }
                }
                SWCCGField.SET -> {
                    card.set?.let { setKey ->
                        this.add(setKey)

                        val set = setRepository.setMap.value?.get(setKey)
                        set?.let {
                            this.add(it.name)
                        }
                    }
                }
                SWCCGField.SIDE -> {
                    card.side?.let { this.add(it) }
                }
                SWCCGField.SUB_TYPE -> {
                    card.front.subType?.let { this.add(it) }
                    card.back?.subType?.let { this.add(it) }
                }
                SWCCGField.TITLE -> {
                    card.front.title?.let { this.add(it) }
                    card.back?.title?.let { this.add(it) }
                }
                SWCCGField.TYPE -> {
                    card.front.type?.let { this.add(it) }
                    card.back?.type?.let { this.add(it) }
                }
                SWCCGField.UNIQUENESS -> {
                    card.front.uniqueness?.let { this.add(it) }
                    card.back?.uniqueness?.let { this.add(it) }
                }
                SWCCGField.PRINTINGS -> {
                    card.printings?.forEach { printing ->
                        printing.set?.let { setKey ->
                            this.add(setKey)

                            val set = setRepository.setMap.value?.get(setKey)
                            set?.let {
                                this.add(it.name)
                            }
                        }
                    }
                }
                SWCCGField.FEROCITY -> {
                    card.front.ferocity?.let { this.add(it) }
                    card.back?.ferocity?.let { this.add(it) }
                }
            }
        }
    }
}
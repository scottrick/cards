package com.hatfat.swccg.data

import java.io.Serializable

data class SWCCGCard(
    val id: Int?,
    val side: String?,
    val rarity: String?,
    val set: String?,
    val front: SWCCGCardFace,
    val back: SWCCGCardFace?,
    val pulledBy: List<String>?,
    val combo: List<String>?,
    val matching: List<String>?,
    val matchingWeapon: List<String>?,
    val legacy: Boolean?
) : Serializable, Comparable<SWCCGCard> {

    val isFlippable: Boolean
        get() = back != null

    override fun compareTo(other: SWCCGCard): Int {
        return front.compareTo(other.front)
    }
}

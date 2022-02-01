package com.hatfat.swccg.data

import java.io.Serializable

@Suppress("unused")
data class SWCCGCardFace(
    val ability: String?,
    val armor: String?,
    val characteristics: MutableList<String>?,
    val darkSideIcons: Int?,
    val deploy: String?,
    val destiny: String?,
    val destinyValues: List<Number>?,
    val extraText: MutableList<String>?,
    val ferocity: String?,
    val forfeit: String?,
    val gametext: String?,
    val hyperspeed: String?,
    val icons: MutableList<String>?,
    val imageUrl: String?,
    val landspeed: String?,
    val lightSideIcons: Int?,
    val lore: String?,
    val maneuver: String?,
    val parsec: String?,
    val politics: String?,
    val power: String?,
    val subType: String?,
    val title: String?,
    val type: String?,
    val uniqueness: String?,
) : Serializable, Comparable<SWCCGCardFace> {

    /* default constructor that gson will call.  otherwise the lazy property will not work */
    constructor() : this(
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null
    )

    @delegate:Transient
    val sortableTitle: String by lazy {
        var result = title ?: ""

        while (result.startsWith("<>")) {
            result = result.removePrefix("<>")
        }

        while (result.startsWith("•")) {
            result = result.removePrefix("•")
        }

        result
    }

    override fun compareTo(other: SWCCGCardFace): Int {
        if (sortableTitle.isBlank() && other.sortableTitle.isBlank()) {
            return 0
        }

        if (sortableTitle.isBlank()) {
            return -1
        }

        if (other.sortableTitle.isBlank()) {
            return 1
        }

        return sortableTitle.compareTo(other.sortableTitle)
    }
}

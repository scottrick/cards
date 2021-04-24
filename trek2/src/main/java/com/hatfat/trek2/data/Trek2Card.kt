package com.hatfat.trek2.data

import java.io.Serializable

data class Trek2Card(
    val name: String?,
    val set: String?,
    val imageFile: String?,
    val rarity: String?,
    val unique: String?,
    val collectorsInfo: String?,
    val type: String?,
    val cost: String?,
    val missionDilemmaType: String?,
    val span: String?,
    val points: String?,
    val quadrant: String?,
    val affiliation: String?,
    val icons: String?,
    val staff: String?,
    val keywords: String?,
    val cardClass: String?,
    val species: String?,
    val skills: String?,
    val intRng: String?,
    val cunWpn: String?,
    val strShd: String?,
    val text: String?,
    val id: Int,
) : Serializable, Comparable<Trek2Card> {

    @delegate:Transient
    val sortableTitle: String by lazy {
        var result = name ?: ""

        while (result.startsWith("\"")) {
            result = result.removePrefix("\"")
        }
        while (result.startsWith("'")) {
            result = result.removePrefix("'")
        }
        while (result.startsWith(".")) {
            result = result.removePrefix(".")
        }
        while (result.startsWith("…")) {
            result = result.removePrefix("…")
        }

        result = result.trim()

        result
    }

    @delegate:Transient
    val hasBack: Boolean by lazy {
        imageFile?.contains(",") == true
    }

    @delegate:Transient
    val frontImageUrl: String by lazy {
        val front = imageFile?.split(",")?.get(0) ?: ""
        "https://raw.githubusercontent.com/eberlems/startrek2e/playable/sets/setimages/general/${front}.jpg"
    }

    @delegate:Transient
    val backImageUrl: String by lazy {
        val back = imageFile?.split(",")?.get(1) ?: ""
        "https://raw.githubusercontent.com/eberlems/startrek2e/playable/sets/setimages/general/${back}.jpg"
    }

    override fun compareTo(other: Trek2Card): Int {
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
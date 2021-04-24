package com.hatfat.trek1.data

import java.io.Serializable

data class Trek1Card(
    val name: String?,
    val set: String?,
    val imageFile: String?,
    val release: String?,
    val info: String?,
    val property: String?,
    val uniqueness: String?,
    val type: String?,
    val missionDilemmaType: String?,
    val affiliation: String?,
    val cardClass: String?,
    val intRng: String?,
    val cunWpn: String?,
    val strShd: String?,
    val points: String?,
    val region: String?,
    val quadrant: String?,
    val span: String?,
    val icons: String?,
    var staff: String?,
    val characteristicsKeywords: String?,
    val requires: String?,
    val persona: String?,
    val command: String?,
    val lore: String?,
    val reports: String?,
    val names: String?,
    val text: String?,
    val id: Int,
) : Serializable, Comparable<Trek1Card> {

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
        "https://raw.githubusercontent.com/eberlems/startrek1e/playable/sets/setimages/general/${front}.jpg"
    }

    @delegate:Transient
    val backImageUrl: String by lazy {
        val back = imageFile?.split(",")?.get(1) ?: ""
        "https://raw.githubusercontent.com/eberlems/startrek1e/playable/sets/setimages/general/${back}.jpg"
    }

    override fun compareTo(other: Trek1Card): Int {
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
package com.hatfat.trek1.data

import com.hatfat.trek1.repo.Trek1MetaDataRepository
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
    val staff: String?,
    val characteristicsKeywords: String?,
    val requires: String?,
    val persona: String?,
    val command: String?,
    val lore: String?,
    val reports: String?,
    val names: String?,
    val text: String?,
) : Serializable, Comparable<Trek1Card> {
    val id: String?
        get() = imageFile

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
    val imageUrl: String by lazy {
        "https://raw.githubusercontent.com/eberlems/startrek1e/playable/sets/setimages/general/${imageFile}.jpg"
    }

    /* special getImageUrl if we try to use the trekcc.org images */
    private fun getImageUrl(metaDataRepository: Trek1MetaDataRepository): String {
        val set = metaDataRepository.sets.value?.get(release)
        val imageUrlDir = set?.imageUrlDir ?: release
        var result = "https://www.trekcc.org/1e/cardimages/${imageUrlDir}/${imageFile}.jpg"

        release?.let {
            if (metaDataRepository.sets.value?.get(release)?.isGif == true) {
                result = "https://www.trekcc.org/1e/cardimages/${release}/${imageFile}.gif"
            }
        }

        return result
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
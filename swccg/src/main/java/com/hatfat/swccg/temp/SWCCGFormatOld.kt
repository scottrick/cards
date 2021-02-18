package com.hatfat.swccg.temp

import java.io.Serializable

data class SWCCGFormatOld(
    val code: String,
    val name: String,
    val all_sets_allowed: Boolean = true,
    val allowed_sets: List<String> = emptyList()
) : Serializable, Comparable<SWCCGFormatOld> {
    override fun toString(): String {
        return name
    }

    override fun compareTo(other: SWCCGFormatOld): Int {
        return name.compareTo(other.name)
    }
}

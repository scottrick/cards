package com.hatfat.swccg.data

import java.io.Serializable

data class SWCCGFormat(
    val code: String,
    val name: String,
    val all_sets_allowed: Boolean = true,
    val allowed_sets: List<String> = emptyList()
) : Serializable, Comparable<SWCCGFormat> {
    override fun toString(): String {
        return name
    }

    override fun compareTo(other: SWCCGFormat): Int {
        return name.compareTo(other.name)
    }
}

package com.hatfat.swccg.data

import java.io.Serializable

data class SWCCGCardIdList(
    val cardIds: IntArray,
) : Serializable {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as SWCCGCardIdList

        return cardIds.contentEquals(other.cardIds)
    }

    override fun hashCode(): Int {
        return cardIds.contentHashCode()
    }
}

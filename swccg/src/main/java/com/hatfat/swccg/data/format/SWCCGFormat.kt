package com.hatfat.swccg.data.format

import java.io.Serializable

data class SWCCGFormat(
    val name: String,
    val code: String,
    val set: List<String>?, //legal set IDs
    val bannedIcons: List<String>?,
    val bannedRarities: List<String>?,
    val banned: List<String>?, //banned gemp card IDs
    val deckSize: Int? = 60,
    val hall: Boolean? = true,
    val playtesting: Boolean? = false
) : Serializable
package com.hatfat.swccg.data.format

data class SWCCGFormat(
    val name: String,
    val code: String,
    val set: List<String>,
    val bannedIcons: List<String>,
    val bannedRarities: List<String>,
    val banned: List<String>
)
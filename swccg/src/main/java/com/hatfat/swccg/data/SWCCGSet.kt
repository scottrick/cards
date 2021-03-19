package com.hatfat.swccg.data

data class SWCCGSet(
    val id: String,
    val name: String,
    val gempName: String?,
    val abbr: String?,
    val legacy: Boolean = false
)
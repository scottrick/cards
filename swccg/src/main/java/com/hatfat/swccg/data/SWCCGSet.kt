package com.hatfat.swccg.data

data class SWCCGSet(
    val id: String,
    var name: String,
    var gempName: String?,
    var abbr: String?,
    var legacy: Boolean = false
)
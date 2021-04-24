package com.hatfat.trek2.data

import java.io.Serializable

data class Trek2Affiliation(
    val displayName: String,
    val missionAbbrs: List<String>,
) : Serializable

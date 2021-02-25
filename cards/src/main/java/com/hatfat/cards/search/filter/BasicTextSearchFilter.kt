package com.hatfat.cards.search.filter

import java.io.Serializable

data class BasicTextSearchFilter(
    val searchOptionKey: Any,
    val searchOptionStringResourceId: Int,
    var isEnabled: Boolean = false
) : Serializable
package com.hatfat.cards.search.param

import java.io.Serializable

data class BasicTextSearchParam(
    val searchOptionKey: Any,
    val searchOptionStringResourceId: Int,
    var isEnabled: Boolean = false
) : Serializable
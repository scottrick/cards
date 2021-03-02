package com.hatfat.cards.search.param

import java.io.Serializable

data class BasicTextSearchParam(
    val searchOptionKey: Any,
    val searchOptionStringResourceId: Int,
    val isEnabledByDefault: Boolean,
    var isEnabled: Boolean = isEnabledByDefault
) : Serializable
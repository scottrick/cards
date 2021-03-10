package com.hatfat.cards.search.filter

import java.io.Serializable

open class TextFilter(
    val liveDataKey: String,
    val extra: Any,
    val textFilterName: String,
    val isEnabledByDefault: Boolean,
    var isEnabled: Boolean = isEnabledByDefault
) : Serializable
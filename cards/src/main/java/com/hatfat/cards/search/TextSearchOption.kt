package com.hatfat.cards.search

import java.io.Serializable

data class TextSearchOption(
    val seachOptionKey: Any,
    val searchOptionStringResourceId: Int,
    var isChecked: Boolean = false
) : Serializable
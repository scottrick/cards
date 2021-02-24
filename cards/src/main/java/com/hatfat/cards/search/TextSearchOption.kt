package com.hatfat.cards.search

data class TextSearchOption(
    val seachOptionKey: Any,
    val searchOptionStringResourceId: Int,
    var isChecked: Boolean = false
)
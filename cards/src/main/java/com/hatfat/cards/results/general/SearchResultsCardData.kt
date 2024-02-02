package com.hatfat.cards.results.general

import androidx.annotation.DrawableRes
import com.hatfat.cards.R

data class SearchResultsCardData(
    var title: String? = null,
    var subtitle: String? = null,
    var listExtraTopText: String? = null,
    var listExtraBottomText: String? = null,
    var carouselExtraText: String? = null,
    var frontImageUrl: String? = null,
    var backImageUrl: String? = null,
    var hasDifferentBack: Boolean = false,
    var infoList: List<String>? = null,
    @DrawableRes
    var cardBackResourceId: Int = R.drawable.background_bordered,
) {
    fun reset() {
        title = null
        subtitle = null
        listExtraTopText = null
        listExtraBottomText = null
        carouselExtraText = null
        frontImageUrl = null
        backImageUrl = null
        hasDifferentBack = false
        infoList = null
        cardBackResourceId = R.drawable.background_bordered
    }

    fun hasExtraInfo(): Boolean {
        return !infoList.isNullOrEmpty()
    }
}

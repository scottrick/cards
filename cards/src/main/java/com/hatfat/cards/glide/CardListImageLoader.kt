package com.hatfat.cards.glide

import android.content.Context
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.hatfat.cards.R
import com.hatfat.cards.results.general.SearchResultsCardData
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Named
import javax.inject.Singleton

@Singleton
class CardListImageLoader @Inject constructor(
    @Named("UseBlurryCardImages") private val shouldUseBlurryImages: Boolean,
    @ApplicationContext private val context: Context,
) {
    fun loadCardImageUrl(
        imageView: ImageView,
        cardData: SearchResultsCardData
    ) {
        var imageRequest = Glide.with(context).load(cardData.frontImageUrl)
            .dontAnimate()
            .transform(
                cardData.cardZoomTransformation
            )
            .placeholder(R.color.backgroundDarkTint)
            .error(R.color.backgroundDark)

        if (shouldUseBlurryImages) {
            imageRequest = imageRequest.override(16, 9)
        }

        imageRequest.into(imageView)
    }
}

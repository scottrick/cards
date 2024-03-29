package com.hatfat.cards.glide

import android.content.Context
import android.graphics.Bitmap
import android.widget.ImageView
import androidx.annotation.DrawableRes
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestBuilder
import com.bumptech.glide.request.target.CustomTarget
import javax.inject.Inject

class CardImageLoader @Inject constructor(
    private val shouldUseBlurryImages: Boolean,
    private val context: Context,
    private val transformations: CardTransformations,
) {
    fun loadCardResourceId(
        @DrawableRes
        cardResourceId: Int,
        imageView: ImageView,
    ) {
        val imageRequest = Glide.with(context).load(cardResourceId)
        val transformedRequest = applyTransformations(imageRequest, cardResourceId)
        transformedRequest.into(imageView)
    }

    fun loadCardImageUrl(
        imageUrl: String?,
        imageView: ImageView,
        @DrawableRes
        placeholderResourceId: Int
    ) {
        val imageRequest = Glide.with(context).load(imageUrl)
        val transformedRequest = applyTransformations(imageRequest, placeholderResourceId)
        transformedRequest.into(imageView)
    }

    fun loadCardImageUrlIntoTarget(
        imageUrl: String?,
        @DrawableRes
        placeholderResourceId: Int,
        target: CustomTarget<Bitmap>
    ) {
        val imageRequest = Glide.with(context).asBitmap().load(imageUrl)
        val transformedRequest = applyTransformations(imageRequest, placeholderResourceId)
        transformedRequest.into(target)
    }

    private fun <T> applyTransformations(
        baseImageRequest: RequestBuilder<T>,
        @DrawableRes
        placeholderResourceId: Int
    ): RequestBuilder<T> {
        var transformedRequest = baseImageRequest
            .dontAnimate()
            .transform(
                *transformations.transformations.toTypedArray()
            )
            .placeholder(placeholderResourceId)
            .error(placeholderResourceId)

        if (shouldUseBlurryImages) {
            transformedRequest = transformedRequest.override(16, 22)
        }

        return transformedRequest
    }
}
package com.hatfat.cards.glide

import android.graphics.Bitmap
import android.graphics.Canvas
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation
import java.nio.charset.Charset
import java.security.MessageDigest
import javax.inject.Inject
import javax.inject.Named
import javax.inject.Singleton
import kotlin.math.roundToInt

@Singleton
class AddCardBorderTransformation @Inject constructor(
    @Named("CardBorderWidthInMillimeters")
    private val cardBorderWidthInMillimeters: Float,
    @Named("CardBorderColor")
    private val cardBorderColor: Int,
) : BitmapTransformation() {

    private val cardWidthInMillimeters = 63.0f

    private val id = AddCardBorderTransformation::class.java.simpleName
    private val idBytes: ByteArray = id.toByteArray(Charset.forName("UTF-8"))

    override fun updateDiskCacheKey(messageDigest: MessageDigest) {
        messageDigest.update(idBytes)
    }

    override fun transform(
        pool: BitmapPool,
        toTransform: Bitmap,
        outWidth: Int,
        outHeight: Int
    ): Bitmap {
        val borderAsPercentageOfWidth = cardBorderWidthInMillimeters / cardWidthInMillimeters
        val centerAsPercentageOfWidth = 1.0f - 2.0f * borderAsPercentageOfWidth

        val borderAsPercentageOfCenter = borderAsPercentageOfWidth / centerAsPercentageOfWidth
        val borderSizeInPixels = (toTransform.width * borderAsPercentageOfCenter).roundToInt()

        val newWidth = toTransform.width + 2 * borderSizeInPixels
        val newHeight = toTransform.height + 2 * borderSizeInPixels

        val transformedBitmap = Bitmap.createBitmap(newWidth, newHeight, toTransform.config)
        transformedBitmap.eraseColor(cardBorderColor)

        val canvas = Canvas(transformedBitmap)
        canvas.drawBitmap(
            toTransform,
            borderSizeInPixels.toFloat(),
            borderSizeInPixels.toFloat(),
            null
        )

        return transformedBitmap
    }

    override fun equals(other: Any?): Boolean {
        return other is AddCardBorderTransformation
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }
}
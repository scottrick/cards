package com.hatfat.cards.glide

import android.graphics.Bitmap
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation
import java.nio.charset.Charset
import java.security.MessageDigest
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
/*
    Values are in inches.
 */
class CardZoomTransformation @Inject constructor(
    private val portraitHorizontalIndent: Float,
    private val portraitTopIndent: Float,
    // If the card is a landscape card, use these values instead.
    private val landscapeHorizontalIndent: Float = 0.0f,
    private val landscapeTopIndent: Float = 0.0f,
) : BitmapTransformation() {

    // Cards are 2.5 by 3.5 inches in size.
    private val baseCardWidth: Float = 2.5f
    private val baseCardHeight: Float = 3.5f

    private val id = CardZoomTransformation::class.java.simpleName
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
        val isLandscape = toTransform.width > toTransform.height
        val cardWidth = if (isLandscape) baseCardHeight else baseCardWidth
        val cardHeight = if (isLandscape) baseCardWidth else baseCardHeight

        val horizontalIndent =
            if (isLandscape) landscapeHorizontalIndent else portraitHorizontalIndent
        val topIndent = if (isLandscape) landscapeTopIndent else portraitTopIndent

        val horizontalIndentPercent = horizontalIndent / cardWidth
        val topIndentPercent = topIndent / cardHeight

        val left = (horizontalIndentPercent * toTransform.width).toInt()
        val width = toTransform.width - (horizontalIndentPercent * toTransform.width * 2.0f).toInt()
        val top = (topIndentPercent * toTransform.height).toInt()
        val height = (width * 9.0f / 16.0f).toInt()

        return Bitmap.createBitmap(toTransform, left, top, width, height)
    }

    override fun equals(other: Any?): Boolean {
        return other is AddCardBorderTransformation
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }
}

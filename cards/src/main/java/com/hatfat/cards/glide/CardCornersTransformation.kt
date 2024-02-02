package com.hatfat.cards.glide

import android.graphics.Bitmap
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation
import com.bumptech.glide.load.resource.bitmap.TransformationUtils
import java.nio.charset.Charset
import java.security.MessageDigest
import javax.inject.Inject
import javax.inject.Named
import javax.inject.Singleton
import kotlin.math.roundToInt

@Singleton
class CardCornersTransformation @Inject constructor(
    @Named("CardCornerRadiusInMillimeters")
    private val cornerRadiusInMillimeters: Float,
) : BitmapTransformation() {

    // Cards are 63 millimeters wide
    private val cornerSizePercent = cornerRadiusInMillimeters / 63.0f

    private val id = CardCornersTransformation::class.java.simpleName
    private val idBytes: ByteArray = id.toByteArray(Charset.forName("UTF-8"))

    override fun updateDiskCacheKey(messageDigest: MessageDigest) {
        messageDigest.update(idBytes)
    }

    override fun transform(
        pool: BitmapPool, toTransform: Bitmap, outWidth: Int, outHeight: Int
    ): Bitmap {
        val cornerSize = (outWidth * cornerSizePercent).roundToInt()
        return TransformationUtils.roundedCorners(pool, toTransform, cornerSize)
    }

    override fun equals(other: Any?): Boolean {
        return other is CardCornersTransformation && this.cornerRadiusInMillimeters == other.cornerRadiusInMillimeters
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }
}
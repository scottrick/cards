package com.hatfat.cards.glide

import android.graphics.Bitmap
import android.graphics.Matrix
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation
import java.nio.charset.Charset
import java.security.MessageDigest

/*
    Rotates cards so they are always rotated the normal "tall" way.
 */
class CardRotationTransformation : BitmapTransformation() {

    private val id = CardRotationTransformation::class.java.simpleName
    private val idBytes: ByteArray = id.toByteArray(Charset.forName("UTF-8"))

    private val rotateRotationAngle = 90f

    override fun updateDiskCacheKey(messageDigest: MessageDigest) {
        messageDigest.update(idBytes)
    }

    override fun transform(pool: BitmapPool, toTransform: Bitmap, outWidth: Int, outHeight: Int): Bitmap {
        val aspectRatio = toTransform.width.toFloat() / toTransform.height.toFloat()
        return if (aspectRatio > 1.0f) {
            val matrix = Matrix()
            matrix.postRotate(rotateRotationAngle)
            Bitmap.createBitmap(toTransform, 0, 0, toTransform.width, toTransform.height, matrix, true)
        } else {
            toTransform
        }
    }

    override fun equals(other: Any?): Boolean {
        return other is CardRotationTransformation
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }
}
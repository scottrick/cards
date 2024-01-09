package com.hatfat.cards.results.swipe

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import androidx.annotation.DrawableRes
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.hatfat.cards.R
import com.hatfat.cards.data.SearchResults
import com.hatfat.cards.util.CardRotationTransformation

abstract class SearchResultsSwipeAdapter(
    private val shouldUsePlayStoreImages: Boolean,
    private val context: Context,
) : RecyclerView.Adapter<SearchResultsSwipeViewHolder>() {

    var isFullscreen: Boolean = false

    private val cardRotationTransformation = CardRotationTransformation()

    var searchResults: SearchResults? = null
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    var onCardSelectedHandler: OnCardSelectedInterface? = null
    var shareCardBitmapInterface: ShareCardBitmapInterface? = null

    var rotated: Boolean = false
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    var flipped: Boolean = false
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): SearchResultsSwipeViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.view_card_full, parent, false)

        val layoutParams: ViewGroup.LayoutParams = if (isFullscreen) {
            ViewGroup.LayoutParams(MATCH_PARENT, MATCH_PARENT)
        } else {
            ViewGroup.LayoutParams(WRAP_CONTENT, MATCH_PARENT)
        }

        view.layoutParams = layoutParams

        return SearchResultsSwipeViewHolder(view, onCardSelectedHandler)
    }

    override fun getItemCount(): Int {
        return searchResults?.size ?: 0
    }

    override fun onBindViewHolder(holder: SearchResultsSwipeViewHolder, position: Int) {
        /* clear old image view */
        holder.imageView.setImageResource(0)
        holder.imageView.rotation = if (rotated) 180.0f else 0.0f

        val imageUrl =
            if (isFlippable(position) && flipped) imageUrlForBack(position) else imageUrlForFront(
                position
            )

        val placeholderResourceId = loadingImageResourceId(position)

        var imageRequest = Glide.with(context).load(imageUrl)
            .transform(cardRotationTransformation)
            .dontAnimate()
            .placeholder(placeholderResourceId)
            .error(placeholderResourceId)

        if (shouldUsePlayStoreImages) {
            imageRequest = imageRequest.override(16, 22)
        }

        imageRequest.into(holder.imageView)
    }

    fun handleSharePressed(position: Int) {
        val imageUrl =
            if (isFlippable(position) && flipped) imageUrlForBack(position) else imageUrlForFront(
                position
            )

        /* get Bitmap and then share it */
        Glide.with(context).asBitmap().load(imageUrl).into(object : CustomTarget<Bitmap>() {
            override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                shareCardBitmapInterface?.shareBitmap(resource)
            }

            override fun onLoadCleared(placeholder: Drawable?) {

            }
        })
    }

    abstract fun isFlippable(position: Int): Boolean
    abstract fun imageUrlForFront(position: Int): String
    abstract fun imageUrlForBack(position: Int): String
    abstract fun extraText(position: Int): String
    abstract fun hasRulings(position: Int): Boolean

    @DrawableRes
    abstract fun loadingImageResourceId(position: Int): Int

    interface OnCardSelectedInterface {
        fun onCardPressed(position: Int)
    }

    interface ShareCardBitmapInterface {
        fun shareBitmap(bitmap: Bitmap)
    }
}
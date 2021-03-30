package com.hatfat.cards.results.swipe

import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.hatfat.cards.R
import com.hatfat.cards.data.SearchResults

abstract class SearchResultsSwipeAdapter constructor(
    private val shouldUsePlayStoreImages: Boolean
) : RecyclerView.Adapter<SearchResultsSwipeViewHolder>() {

    var isFullscreen: Boolean = false
    var isLandscape: Boolean = false

    var searchResults: SearchResults? = null
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    var onCardSelectedHandler: OnCardSelectedInterface? = null

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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchResultsSwipeViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.view_card_full, parent, false)

        val layoutParams: ViewGroup.LayoutParams = if (isFullscreen) {
            ViewGroup.LayoutParams(MATCH_PARENT, MATCH_PARENT)
        } else {
            if (isLandscape) {
                ViewGroup.LayoutParams(MATCH_PARENT, WRAP_CONTENT)
            } else {
                ViewGroup.LayoutParams(WRAP_CONTENT, MATCH_PARENT)
            }
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

        val imageUrl = if (isFlippable(position) && flipped) imageUrlForBack(position) else imageUrlForFront(position)

        var imageRequest = Glide.with(holder.imageView.context).load(imageUrl)
            .dontAnimate()
            .placeholder(R.mipmap.loading_large)
            .error(R.mipmap.loading_large)

        if (shouldUsePlayStoreImages) {
            imageRequest = imageRequest.override(16, 22)
        }

        imageRequest.into(holder.imageView)
    }

    abstract fun isFlippable(position: Int): Boolean
    abstract fun imageUrlForFront(position: Int): String
    abstract fun imageUrlForBack(position: Int): String
    abstract fun extraText(position: Int): String

    interface OnCardSelectedInterface {
        fun onCardPressed(position: Int)
    }
}
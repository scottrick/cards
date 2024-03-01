package com.hatfat.cards.results.carousel

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.hatfat.cards.R
import com.hatfat.cards.data.SearchResults
import com.hatfat.cards.glide.CardImageLoader
import com.hatfat.cards.results.general.SearchResultsCardData
import com.hatfat.cards.results.general.SearchResultsDataProvider
import javax.inject.Inject
import javax.inject.Named
import kotlin.math.roundToInt

class SearchResultsCarouselAdapter @Inject constructor(
    private val searchResultsDataProvider: SearchResultsDataProvider,
    @Named("StandardCardImageLoader")
    private val cardImageLoader: CardImageLoader,
    @Named("ShareCardImageLoader")
    private val shareCardImageLoader: CardImageLoader,
) : RecyclerView.Adapter<SearchResultsCarouselViewHolder>() {

    private var hasInitParentDimensions = false
    private var maxImageWidth: Int = 0
    private var maxImageHeight: Int = 0
    private var maxImageAspectRatio: Float = 1f

    private val imageAspectRatio = 350.0f / 490.0f

    var searchResults: SearchResults? = null
        @SuppressLint("NotifyDataSetChanged")
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    var onCardSelectedHandler: OnCardSelectedInterface? = null
    var shareCardBitmapInterface: ShareCardBitmapInterface? = null

    var rotated: Boolean = false
        @SuppressLint("NotifyDataSetChanged")
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    var flipped: Boolean = false
        @SuppressLint("NotifyDataSetChanged")
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    // Reusable search results card data
    val cardData = SearchResultsCardData()

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): SearchResultsCarouselViewHolder {
        if (!hasInitParentDimensions) {
            val carouselAdjacentWidth =
                parent.resources.getDimensionPixelSize(R.dimen.carousel_adjacent_width)
            val carouselSpacing = parent.resources.getDimensionPixelSize(R.dimen.carousel_spacing)

            maxImageWidth = parent.width - 2 * (carouselAdjacentWidth + carouselSpacing)
            maxImageHeight = parent.height
            maxImageAspectRatio = maxImageWidth.toFloat() / maxImageHeight.toFloat()
            hasInitParentDimensions = true
        }

        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.view_card_carousel, parent, false)

        return SearchResultsCarouselViewHolder(view, onCardSelectedHandler)
    }

    override fun onBindViewHolder(holder: SearchResultsCarouselViewHolder, position: Int) {
        searchResults?.let {
            cardData.reset()
            searchResultsDataProvider.getCardDataForPosition(it, position, cardData)
            /* clear old image view */
            holder.imageView.setImageResource(0)
            holder.imageView.rotation = if (rotated) 180.0f else 0.0f

            /* update aspect ratio */
            val targetImageWidth: Int = if (imageAspectRatio < maxImageAspectRatio) {
                (maxImageHeight * imageAspectRatio).roundToInt()
            } else {
                maxImageWidth
            }

            holder.imageView.layoutParams = RecyclerView.LayoutParams(
                targetImageWidth,
                RecyclerView.LayoutParams.MATCH_PARENT
            )

            val imageUrl =
                if (cardData.hasDifferentBack && flipped) cardData.backImageUrl else cardData.frontImageUrl

            cardImageLoader.loadCardImageUrl(
                imageUrl,
                holder.imageView,
                cardData.cardBackResourceId
            )
        }
    }


    override fun getItemCount(): Int {
        return searchResults?.size ?: 0
    }

    fun handleSharePressed(position: Int) {
        searchResults?.let {
            cardData.reset()
            searchResultsDataProvider.getCardDataForPosition(it, position, cardData)

            val imageUrl =
                if (cardData.hasDifferentBack && flipped) cardData.backImageUrl else cardData.frontImageUrl

            /* get Bitmap and then share it */
            shareCardImageLoader.loadCardImageUrlIntoTarget(imageUrl, cardData.cardBackResourceId,
                object : CustomTarget<Bitmap>() {
                    override fun onResourceReady(
                        resource: Bitmap,
                        transition: Transition<in Bitmap>?
                    ) {
                        shareCardBitmapInterface?.shareBitmap(cardData.title, resource)
                    }

                    override fun onLoadCleared(placeholder: Drawable?) {

                    }
                }
            )
        }
    }

    interface OnCardSelectedInterface {
        fun onCardPressed(position: Int)
    }

    interface ShareCardBitmapInterface {
        fun shareBitmap(cardTitle: String?, bitmap: Bitmap)
    }
}

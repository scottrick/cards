package com.hatfat.cards.results.carousel

import android.view.View
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.hatfat.cards.R

class SearchResultsCarouselViewHolder(
    itemView: View,
    onCardSelected: SearchResultsCarouselAdapter.OnCardSelectedInterface?
) : RecyclerView.ViewHolder(itemView) {

    init {
        itemView.setOnClickListener {
            onCardSelected?.onCardPressed(bindingAdapterPosition)
        }
    }

    val imageView: ImageView = itemView.findViewById(R.id.view_card_full_imageview)
}

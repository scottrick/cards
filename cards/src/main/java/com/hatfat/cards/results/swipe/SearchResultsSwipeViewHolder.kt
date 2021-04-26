package com.hatfat.cards.results.swipe

import android.view.View
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.hatfat.cards.R

class SearchResultsSwipeViewHolder(
    itemView: View,
    onCardSelected: SearchResultsSwipeAdapter.OnCardSelectedInterface?
) : RecyclerView.ViewHolder(itemView) {

    init {
        itemView.setOnClickListener {
            onCardSelected?.onCardPressed(bindingAdapterPosition)
        }

        itemView.setOnLongClickListener {
            onCardSelected?.onCardLongPressed(bindingAdapterPosition)
            true
        }
    }

    val imageView: ImageView = itemView.findViewById(R.id.view_card_full_imageview)
}

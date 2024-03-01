package com.hatfat.cards.results.list

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.hatfat.cards.R

class SearchResultsListViewHolder(
    itemView: View, onCardSelected: SearchResultsListAdapter.OnCardSelectedInterface?
) : RecyclerView.ViewHolder(itemView) {

    init {
        itemView.setOnClickListener {
            onCardSelected?.onCardPressed(bindingAdapterPosition)
        }
    }

    val titleTextView: TextView = itemView.findViewById(R.id.card_title)
    val subtitleTextView: TextView = itemView.findViewById(R.id.card_subtitle)
    val extraTextView: TextView = itemView.findViewById(R.id.card_extra_text)
    val imageView: ImageView = itemView.findViewById(R.id.card_imageview)
}
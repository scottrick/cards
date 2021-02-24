package com.hatfat.cards.results

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.hatfat.cards.R

class SearchResultViewHolder(
    itemView: View
) : RecyclerView.ViewHolder(itemView) {

    val titleTextView = itemView.findViewById<TextView>(R.id.card_title)
    val subtitleTextView = itemView.findViewById<TextView>(R.id.card_subtitle)
    val extraTextView = itemView.findViewById<TextView>(R.id.card_extra)
    val imageView = itemView.findViewById<ImageView>(R.id.card_imageview)
}
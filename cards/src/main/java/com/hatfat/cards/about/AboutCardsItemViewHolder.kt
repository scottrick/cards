package com.hatfat.cards.about

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.hatfat.cards.R

class AboutCardsItemViewHolder(
    itemView: View
) : RecyclerView.ViewHolder(itemView) {
    val aboutTextView: TextView = itemView.findViewById(R.id.about_textview)
}
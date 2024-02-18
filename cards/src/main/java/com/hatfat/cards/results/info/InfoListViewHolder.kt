package com.hatfat.cards.results.info

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.hatfat.cards.R

class InfoListViewHolder(
    itemView: View,
) : RecyclerView.ViewHolder(itemView) {
    val textView: TextView = itemView.findViewById(R.id.info_list_item_textview)
}

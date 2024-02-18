package com.hatfat.cards.search

import android.view.View
import android.widget.Button
import androidx.recyclerview.widget.RecyclerView
import com.hatfat.cards.R
import com.hatfat.cards.search.filter.advanced.AdvancedFilterAdapter

class CardSearchResetViewHolder(
    itemView: View,
    advancedFilterHandler: AdvancedFilterAdapter.AdvancedFilterHandlerInterface?
) : RecyclerView.ViewHolder(itemView) {
    private val resetButton: Button = itemView.findViewById(R.id.reset_button)
    private val searchButton: Button = itemView.findViewById(R.id.search_button)

    init {
        resetButton.setOnClickListener {
            advancedFilterHandler?.onResetPressed()
        }
        searchButton.setOnClickListener {
            advancedFilterHandler?.onSearchPressed()
        }
    }
}

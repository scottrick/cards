package com.hatfat.cards.results.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hatfat.cards.R
import com.hatfat.cards.results.SearchResults

abstract class SearchResultsListAdapter : RecyclerView.Adapter<SearchResultsListViewHolder>() {

    var searchResults: SearchResults? = null
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    var onCardSelectedHandler: OnCardSelectedInterface? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchResultsListViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.view_search_results_row, parent, false)
        return SearchResultsListViewHolder(view, onCardSelectedHandler)
    }

    override fun getItemCount(): Int {
        return searchResults?.size ?: 0
    }

    interface OnCardSelectedInterface {
        fun onCardPressed(position: Int)
    }
}
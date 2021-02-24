package com.hatfat.cards.results

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hatfat.cards.R

abstract class SearchResultsListAdapter : RecyclerView.Adapter<SearchResultViewHolder>() {

    var searchResults: SearchResults? = null
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchResultViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.view_search_results_row, parent, false)
        return SearchResultViewHolder(view)
    }

    override fun getItemCount(): Int {
        return searchResults?.size ?: 0
    }
}
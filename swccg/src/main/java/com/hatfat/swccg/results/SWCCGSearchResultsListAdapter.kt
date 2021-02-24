package com.hatfat.swccg.results

import com.hatfat.cards.results.SearchResultViewHolder
import com.hatfat.cards.results.SearchResultsListAdapter
import javax.inject.Inject

class SWCCGSearchResultsListAdapter @Inject constructor() : SearchResultsListAdapter() {

    override fun onBindViewHolder(holder: SearchResultViewHolder, position: Int) {
        searchResults?.getResult(position)?.let {
            holder.titleTextView.text = "catfat $it"
        }
    }
}
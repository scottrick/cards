package com.hatfat.cards.results.list

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hatfat.cards.R
import com.hatfat.cards.data.SearchResults
import com.hatfat.cards.results.general.SearchResultsCardData
import com.hatfat.cards.results.general.SearchResultsDataProvider
import com.hatfat.cards.glide.CardImageLoader
import javax.inject.Inject

class SearchResultsListAdapter @Inject constructor(
    private val searchResultsDataProvider: SearchResultsDataProvider,
    private val cardImageLoader: CardImageLoader,
) : RecyclerView.Adapter<SearchResultsListViewHolder>() {

    var searchResults: SearchResults? = null
        @SuppressLint("NotifyDataSetChanged")
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    var onCardSelectedHandler: OnCardSelectedInterface? = null

    // Reusable search results card data
    val cardData = SearchResultsCardData()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchResultsListViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.view_search_results_row, parent, false)
        return SearchResultsListViewHolder(view, onCardSelectedHandler)
    }

    override fun onBindViewHolder(holder: SearchResultsListViewHolder, position: Int) {
        searchResults?.let {
            cardData.reset()
            searchResultsDataProvider.getCardDataForPosition(it, position, cardData)
            holder.titleTextView.text = cardData.title
            holder.subtitleTextView.text = cardData.subtitle
            holder.extraTopTextView.text = cardData.listExtraTopText
            holder.extraBottomTextView.text = cardData.listExtraBottomText

            cardImageLoader.loadCardImageUrl(
                cardData.frontImageUrl,
                holder.imageView,
                cardData.cardBackResourceId
            )
        }
    }

    override fun getItemCount(): Int {
        return searchResults?.size ?: 0
    }

    interface OnCardSelectedInterface {
        fun onCardPressed(position: Int)
    }
}
package com.hatfat.cards.results.list

import android.annotation.SuppressLint
import android.content.res.Resources
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hatfat.cards.R
import com.hatfat.cards.data.SearchResults
import com.hatfat.cards.glide.CardListImageLoader
import com.hatfat.cards.results.general.SearchResultsCardData
import com.hatfat.cards.results.general.SearchResultsDataProvider
import javax.inject.Inject

class SearchResultsListAdapter @Inject constructor(
    private val searchResultsDataProvider: SearchResultsDataProvider,
    private val cardListImageLoader: CardListImageLoader,
    private val resources: Resources,
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
            @Suppress("DEPRECATION")
            val accentColor = resources.getColor(cardData.cardAccentColor)
            holder.subtitleTextView.setTextColor(accentColor)
            holder.subtitleTextView.text = cardData.subtitle
            holder.extraTextView.text = cardData.listExtraText
            holder.imageView.setBackgroundColor(accentColor)
            cardListImageLoader.loadCardImageUrl(
                holder.imageView,
                cardData,
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
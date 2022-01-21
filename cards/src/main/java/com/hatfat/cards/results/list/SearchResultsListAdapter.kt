package com.hatfat.cards.results.list

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hatfat.cards.R
import com.hatfat.cards.data.SearchResults

abstract class SearchResultsListAdapter : RecyclerView.Adapter<SearchResultsListViewHolder>() {

    var searchResults: SearchResults? = null
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    var onCardSelectedHandler: OnCardSelectedInterface? = null
    var onCurrentItemImageHasLoaded: CurrentItemImageHasLoadedInterface? = null

    val itemLoadedHandler = object : ItemImageHasLoadedInterface {
        override fun onItemImageHasLoaded(position: Int) {
            searchResults?.let {
                if (it.currentPosition == position) {
                    /* the current item's image has finished being loaded, so notify our listener */
                    Log.e("catfat", "CURRENT item has loaded! " + position)
                    onCurrentItemImageHasLoaded?.onCurrentItemImageHasLoaded()
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchResultsListViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.view_search_results_row, parent, false)
        return SearchResultsListViewHolder(view, onCardSelectedHandler)
    }

    protected fun bindSharedImageViewTransitionForPosition(
        holder: SearchResultsListViewHolder,
        position: Int
    ) {
        holder.imageView.transitionName =
            SearchResultsListFragment.getSharedImageViewTransitionNameForPosition(position)
    }

    override fun getItemCount(): Int {
        return searchResults?.size ?: 0
    }

    interface OnCardSelectedInterface {
        fun onCardPressed(position: Int)
    }

    interface CurrentItemImageHasLoadedInterface {
        fun onCurrentItemImageHasLoaded()
    }

    interface ItemImageHasLoadedInterface {
        fun onItemImageHasLoaded(position: Int)
    }
}
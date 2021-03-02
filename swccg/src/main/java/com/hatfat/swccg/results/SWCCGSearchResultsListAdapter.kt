package com.hatfat.swccg.results

import com.bumptech.glide.Glide
import com.hatfat.cards.results.list.SearchResultsListAdapter
import com.hatfat.cards.results.list.SearchResultsListViewHolder
import com.hatfat.swccg.R
import com.hatfat.swccg.repo.SWCCGCardsRepository
import com.hatfat.swccg.search.SWCCGSearchResults
import javax.inject.Inject
import javax.inject.Named

class SWCCGSearchResultsListAdapter @Inject constructor(
    private val cardRepository: SWCCGCardsRepository,
    @Named("should use playstore images") private val shouldUsePlayStoreImages: Boolean
) : SearchResultsListAdapter() {

    override fun onBindViewHolder(holder: SearchResultsListViewHolder, position: Int) {
        (searchResults as SWCCGSearchResults).also {
            val cardId = it.getResult(position)
            cardRepository.cardsMap.value?.get(cardId)?.let { card ->
                holder.titleTextView.text = card.front.title
                holder.subtitleTextView.text = card.front.type
                holder.extraTopTextView.text = card.rarity
                holder.extraBottomTextView.text = card.set

                /* clear old image view */
                holder.imageView.setImageResource(0)

                if (shouldUsePlayStoreImages) {
                    Glide.with(holder.imageView.context).load(card.front.imageUrl).override(16, 22)
                        .placeholder(R.mipmap.loading_large).into(holder.imageView)
                } else {
                    Glide.with(holder.imageView.context).load(card.front.imageUrl).placeholder(R.mipmap.loading_large)
                        .into(holder.imageView)
                }
            }
        }
    }
}
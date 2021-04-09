package com.hatfat.meccg.results

import com.bumptech.glide.Glide
import com.hatfat.cards.results.list.SearchResultsListAdapter
import com.hatfat.cards.results.list.SearchResultsListViewHolder
import com.hatfat.meccg.R
import com.hatfat.meccg.repo.MECCGCardsRepository
import com.hatfat.meccg.search.MECCGSearchResults
import javax.inject.Inject
import javax.inject.Named

class MECCGSearchResultsListAdapter @Inject constructor(
    private val cardRepository: MECCGCardsRepository,
//    private val setRepository: SWCCGSetRepository,
    @Named("should use playstore images") private val shouldUsePlayStoreImages: Boolean
) : SearchResultsListAdapter() {

    override fun onBindViewHolder(holder: SearchResultsListViewHolder, position: Int) {
        (searchResults as MECCGSearchResults).also {
            val cardId = it.getResult(position)
            cardRepository.cardsMap.value?.get(cardId)?.let { card ->
                holder.titleTextView.text = card.nameEN
                holder.subtitleTextView.text = card.primary
                holder.extraTopTextView.text = card.rarity
                holder.extraBottomTextView.text = card.set

                /* clear old image view */
                holder.imageView.setImageResource(0)

                if (shouldUsePlayStoreImages) {
                    Glide.with(holder.imageView.context).load(card.imageUrl).override(16, 22)
                        .dontAnimate()
                        .placeholder(R.mipmap.loading_large).into(holder.imageView)
                } else {
                    Glide.with(holder.imageView.context).load(card.imageUrl)
                        .dontAnimate()
                        .placeholder(R.mipmap.loading_large).into(holder.imageView)
                }
            }
        }
    }
}

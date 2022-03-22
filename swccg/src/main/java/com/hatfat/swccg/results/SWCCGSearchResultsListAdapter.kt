package com.hatfat.swccg.results

import com.bumptech.glide.Glide
import com.hatfat.cards.results.list.SearchResultsListAdapter
import com.hatfat.cards.results.list.SearchResultsListViewHolder
import com.hatfat.swccg.repo.SWCCGCardRepository
import com.hatfat.swccg.repo.SWCCGSetRepository
import com.hatfat.swccg.search.SWCCGSearchResults
import javax.inject.Inject
import javax.inject.Named

class SWCCGSearchResultsListAdapter @Inject constructor(
    private val cardRepository: SWCCGCardRepository,
    private val setRepository: SWCCGSetRepository,
    @Named("should use playstore images") private val shouldUsePlayStoreImages: Boolean
) : SearchResultsListAdapter() {

    private val cardBackHelper = SWCCGCardBackHelper()

    override fun onBindViewHolder(holder: SearchResultsListViewHolder, position: Int) {
        (searchResults as SWCCGSearchResults).also {
            val cardId = it.getResult(position)
            cardRepository.cardsMap.value?.get(cardId)?.let { card ->
                val setAbbr = setRepository.setMap.value?.get(card.set)?.abbr ?: "Unknown"
                holder.titleTextView.text = card.front.title
                holder.subtitleTextView.text = card.front.type
                holder.extraTopTextView.text = card.rarity
                holder.extraBottomTextView.text = setAbbr

                val loadingResourceId = cardBackHelper.getCardBackResourceId(card)

                /* clear old image view */
                holder.imageView.setImageResource(0)

                if (shouldUsePlayStoreImages) {
                    Glide.with(holder.imageView.context).load(card.front.imageUrl).override(16, 22)
                        .dontAnimate()
                        .error(loadingResourceId)
                        .placeholder(loadingResourceId).into(holder.imageView)
                } else {
                    Glide.with(holder.imageView.context).load(card.front.imageUrl)
                        .dontAnimate()
                        .error(loadingResourceId)
                        .placeholder(loadingResourceId).into(holder.imageView)
                }
            }
        }
    }
}
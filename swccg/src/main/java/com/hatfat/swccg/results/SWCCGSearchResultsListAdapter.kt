package com.hatfat.swccg.results

import com.bumptech.glide.Glide
import com.hatfat.cards.results.list.SearchResultsListAdapter
import com.hatfat.cards.results.list.SearchResultsListGlideRequestListener
import com.hatfat.cards.results.list.SearchResultsListViewHolder
import com.hatfat.swccg.R
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

    override fun onBindViewHolder(holder: SearchResultsListViewHolder, position: Int) {
        bindSharedImageViewTransitionForPosition(holder, position)

        (searchResults as SWCCGSearchResults).also {
            val cardId = it.getResult(position)
            cardRepository.cardsMap.value?.get(cardId)?.let { card ->
                val setAbbr = setRepository.setMap.value?.get(card.set)?.abbr ?: "Unknown"
                holder.titleTextView.text = card.front.title
                holder.subtitleTextView.text = card.front.type
                holder.extraTopTextView.text = card.rarity
                holder.extraBottomTextView.text = setAbbr

                /* clear old image view */
                holder.imageView.setImageResource(0)

                if (shouldUsePlayStoreImages) {
                    Glide.with(holder.imageView.context)
                        .load(card.front.imageUrl)
                        .listener(
                            SearchResultsListGlideRequestListener(
                                itemLoadedHandler,
                                position
                            )
                        )
                        .override(16, 22)
                        .placeholder(R.mipmap.loading_large)
                        .into(holder.imageView)
                } else {
                    Glide.with(holder.imageView.context)
                        .load(card.front.imageUrl)
                        .listener(
                            SearchResultsListGlideRequestListener(
                                itemLoadedHandler,
                                position
                            )
                        )
                        .placeholder(R.mipmap.loading_large)
                        .into(holder.imageView)
                }
            }
        }
    }
}
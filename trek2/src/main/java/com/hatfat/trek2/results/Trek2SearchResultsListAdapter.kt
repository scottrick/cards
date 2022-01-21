package com.hatfat.trek2.results

import com.bumptech.glide.Glide
import com.hatfat.cards.results.list.SearchResultsListAdapter
import com.hatfat.cards.results.list.SearchResultsListViewHolder
import com.hatfat.trek2.R
import com.hatfat.trek2.repo.Trek2CardRepository
import com.hatfat.trek2.repo.Trek2SetRepository
import com.hatfat.trek2.search.Trek2SearchResults
import javax.inject.Inject
import javax.inject.Named

class Trek2SearchResultsListAdapter @Inject constructor(
    private val cardRepository: Trek2CardRepository,
    private val setRepository: Trek2SetRepository,
    @Named("should use playstore images") private val shouldUsePlayStoreImages: Boolean
) : SearchResultsListAdapter() {

    override fun onBindViewHolder(holder: SearchResultsListViewHolder, position: Int) {
        bindSharedImageViewTransitionForPosition(holder, position)

        (searchResults as Trek2SearchResults).also {
            val cardId = it.getResult(position)
            cardRepository.cardsMap.value?.get(cardId)?.let { card ->
                holder.titleTextView.text = card.name
                holder.subtitleTextView.text = card.type
                holder.extraTopTextView.text = card.rarity

                val set = setRepository.setMap.value?.get(card.set ?: "")
                holder.extraBottomTextView.text = set?.name ?: "Unknown"

                /* clear old image view */
                holder.imageView.setImageResource(0)

                if (shouldUsePlayStoreImages) {
                    Glide.with(holder.imageView.context).load(card.frontImageUrl).override(16, 22)
                        .dontAnimate()
                        .placeholder(R.mipmap.loading_large).into(holder.imageView)
                } else {
                    Glide.with(holder.imageView.context).load(card.frontImageUrl)
                        .dontAnimate()
                        .placeholder(R.mipmap.loading_large).into(holder.imageView)
                }
            }
        }
    }
}
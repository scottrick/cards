package com.hatfat.trek1.results

import com.bumptech.glide.Glide
import com.hatfat.cards.results.list.SearchResultsListAdapter
import com.hatfat.cards.results.list.SearchResultsListViewHolder
import com.hatfat.trek1.R
import com.hatfat.trek1.repo.Trek1CardRepository
import com.hatfat.trek1.repo.Trek1SetRepository
import com.hatfat.trek1.search.Trek1SearchResults
import javax.inject.Inject
import javax.inject.Named

class Trek1SearchResultsListAdapter @Inject constructor(
    private val cardRepository: Trek1CardRepository,
    private val setRepository: Trek1SetRepository,
    @Named("should use playstore images") private val shouldUsePlayStoreImages: Boolean
) : SearchResultsListAdapter() {

    override fun onBindViewHolder(holder: SearchResultsListViewHolder, position: Int) {
        (searchResults as Trek1SearchResults).also {
            val cardId = it.getResult(position)
            cardRepository.cardsMap.value?.get(cardId)?.let { card ->
                holder.titleTextView.text = card.name
                holder.subtitleTextView.text = card.type
                holder.extraTopTextView.text = card.info

                val set = setRepository.getSetForCard(card)
                holder.extraBottomTextView.text = set.abbr ?: "Unknown"

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
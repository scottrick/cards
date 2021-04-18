package com.hatfat.trek2.results

import com.hatfat.cards.results.list.SearchResultsListAdapter
import com.hatfat.cards.results.list.SearchResultsListViewHolder
import com.hatfat.trek2.search.Trek2SearchResults
import javax.inject.Inject
import javax.inject.Named

class Trek2SearchResultsListAdapter @Inject constructor(
//    private val cardRepository: MECCGCardRepository,
    @Named("should use playstore images") private val shouldUsePlayStoreImages: Boolean
) : SearchResultsListAdapter() {

    override fun onBindViewHolder(holder: SearchResultsListViewHolder, position: Int) {
        (searchResults as Trek2SearchResults).also {
//            val cardId = it.getResult(position)
//            cardRepository.cardsMap.value?.get(cardId)?.let { card ->
//                holder.titleTextView.text = card.nameEN
//                holder.subtitleTextView.text = card.primary
//                holder.extraTopTextView.text = card.rarity
//                holder.extraBottomTextView.text = card.set
//
//                /* clear old image view */
//                holder.imageView.setImageResource(0)
//
//                if (shouldUsePlayStoreImages) {
//                    Glide.with(holder.imageView.context).load(card.imageUrl).override(16, 22)
//                        .dontAnimate()
//                        .placeholder(R.mipmap.loading_large).into(holder.imageView)
//                } else {
//                    Glide.with(holder.imageView.context).load(card.imageUrl)
//                        .dontAnimate()
//                        .placeholder(R.mipmap.loading_large).into(holder.imageView)
//                }
//            }
        }
    }
}
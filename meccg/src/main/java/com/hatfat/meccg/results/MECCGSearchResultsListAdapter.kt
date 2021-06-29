package com.hatfat.meccg.results

import android.view.View
import com.bumptech.glide.Glide
import com.hatfat.cards.results.list.SearchResultsListAdapter
import com.hatfat.cards.results.list.SearchResultsListViewHolder
import com.hatfat.meccg.R
import com.hatfat.meccg.repo.MECCGCardRepository
import com.hatfat.meccg.search.MECCGSearchResults
import javax.inject.Inject
import javax.inject.Named

class MECCGSearchResultsListAdapter @Inject constructor(
    private val cardRepository: MECCGCardRepository,
    @Named("should use playstore images") private val shouldUsePlayStoreImages: Boolean
) : SearchResultsListAdapter() {

    override fun onBindViewHolder(holder: SearchResultsListViewHolder, position: Int) {
        (searchResults as MECCGSearchResults).also {
            val cardId = it.getResult(position)
            cardRepository.cardsMap.value?.get(cardId)?.let { card ->
                holder.titleTextView.text = card.nameEN
                holder.subtitleTextView.text = card.primary
                holder.extraBottomTextView.text = card.set

                /* Dreamcards don't have rarity, so hide the textview in those cases */
                if (card.dreamcard == true) {
                    holder.extraTopTextView.text = null
                    holder.extraTopTextView.visibility = View.GONE
                } else {
                    holder.extraTopTextView.text = card.precise
                    holder.extraTopTextView.visibility = View.VISIBLE
                }

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
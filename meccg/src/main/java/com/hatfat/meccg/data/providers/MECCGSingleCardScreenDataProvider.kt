package com.hatfat.meccg.data.providers

import android.content.Context
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.hatfat.cards.data.card.SingleCardData
import com.hatfat.cards.data.card.SingleCardScreenData
import com.hatfat.cards.data.card.SingleCardScreenDataProvider
import com.hatfat.cards.util.CardRotationTransformation
import com.hatfat.meccg.repo.MECCGCardRepository
import com.hatfat.meccg.results.MECCGCardBackHelper
import com.hatfat.meccg.search.MECCGSearchResults
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class MECCGSingleCardScreenDataProvider @Inject constructor(
    private val cardRepo: MECCGCardRepository,
    @ApplicationContext val context: Context,
) : SingleCardScreenDataProvider {

    private val cardRotationTransformation = CardRotationTransformation()
    private val cardBackHelper = MECCGCardBackHelper()

    override fun getSingleCardScreenDataFromCard(cardData: SingleCardData): SingleCardScreenData {
        (cardData.searchResults as MECCGSearchResults).also {
            val cardId = it.getResult(cardData.position)
            val card = cardRepo.cardsMap.value?.get(cardId)

            return SingleCardScreenData { imageView: ImageView ->
                val placeholderResourceId = cardBackHelper.getCardBackResourceId(card)
                val imageUrl = card?.imageUrl

                if (imageUrl != null) {
                    val imageRequest = Glide.with(context).load(imageUrl)
                        .transform(cardRotationTransformation)
                        .dontAnimate()
                        .placeholder(placeholderResourceId)
                        .error(placeholderResourceId)

                    imageRequest.into(imageView)
                } else {
                    imageView.setImageResource(placeholderResourceId)
                }
            }
        }
    }
}

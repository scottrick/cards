package com.hatfat.swccg.data.providers

import android.content.Context
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.hatfat.cards.data.card.SingleCardData
import com.hatfat.cards.data.card.SingleCardScreenData
import com.hatfat.cards.data.card.SingleCardScreenDataProvider
import com.hatfat.cards.util.CardRotationTransformation
import com.hatfat.swccg.repo.SWCCGCardRepository
import com.hatfat.swccg.results.SWCCGCardBackHelper
import com.hatfat.swccg.search.SWCCGSearchResults
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class SWCCGSingleCardScreenDataProvider @Inject constructor(
    private val cardRepo: SWCCGCardRepository,
    @ApplicationContext val context: Context,
) : SingleCardScreenDataProvider {

    private val cardRotationTransformation = CardRotationTransformation()
    private val cardBackHelper = SWCCGCardBackHelper()

    override fun getSingleCardScreenDataFromCard(cardData: SingleCardData): SingleCardScreenData {
        (cardData.searchResults as SWCCGSearchResults).also {
            val cardId = it.getResult(cardData.position)
            val card = cardRepo.cardsMap.value?.get(cardId)

            return SingleCardScreenData { imageView: ImageView ->
                val placeholderResourceId = cardBackHelper.getCardBackResourceId(card)

                val imageUrl =
                    if (cardData.isFlipped) card?.back?.imageUrl else card?.front?.imageUrl

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
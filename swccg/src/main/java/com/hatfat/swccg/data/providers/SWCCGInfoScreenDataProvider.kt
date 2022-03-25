package com.hatfat.swccg.data.providers

import android.content.Context
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.hatfat.cards.data.card.SingleCardData
import com.hatfat.cards.info.InfoScreenData
import com.hatfat.cards.info.InfoScreenDataProvider
import com.hatfat.cards.util.CardRotationTransformation
import com.hatfat.swccg.repo.SWCCGCardRepository
import com.hatfat.swccg.results.SWCCGCardBackHelper
import com.hatfat.swccg.search.SWCCGSearchResults
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class SWCCGInfoScreenDataProvider @Inject constructor(
    val cardRepo: SWCCGCardRepository,
    @ApplicationContext val context: Context,
) : InfoScreenDataProvider {

    private val cardRotationTransformation = CardRotationTransformation()
    private val cardBackHelper = SWCCGCardBackHelper()

    override fun getInfoScreenDataFromCard(cardData: SingleCardData): InfoScreenData {
        (cardData.searchResults as SWCCGSearchResults).also {
            val cardId = it.getResult(cardData.position)
            val card = cardRepo.cardsMap.value?.get(cardId)
            val rulings = card?.rulings ?: emptyList()

            return InfoScreenData(
                card?.front?.title ?: "Unknown",
                "Rulings",
                rulings,
                { frontImageView: ImageView ->
                    val placeholderResourceId = cardBackHelper.getCardBackResourceId(card)

                    if (card?.front?.imageUrl != null) {
                        val imageRequest = Glide.with(context).load(card.front.imageUrl)
                            .transform(cardRotationTransformation)
                            .dontAnimate()
                            .placeholder(placeholderResourceId)
                            .error(placeholderResourceId)

                        imageRequest.into(frontImageView)
                    } else {
                        frontImageView.setImageResource(placeholderResourceId)
                    }
                },
                { backImageView: ImageView ->
                    val placeholderResourceId = cardBackHelper.getCardBackResourceId(card)

                    if (card?.back?.imageUrl != null) {
                        val imageRequest = Glide.with(context).load(card.back.imageUrl)
                            .transform(cardRotationTransformation)
                            .dontAnimate()
                            .placeholder(placeholderResourceId)
                            .error(placeholderResourceId)

                        imageRequest.into(backImageView)
                    } else {
                        backImageView.setImageResource(placeholderResourceId)
                    }
                },
            )
        }
    }
}

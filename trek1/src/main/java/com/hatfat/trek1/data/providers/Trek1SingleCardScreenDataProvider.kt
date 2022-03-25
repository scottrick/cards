package com.hatfat.trek1.data.providers

import android.content.Context
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.hatfat.cards.data.card.SingleCardData
import com.hatfat.cards.data.card.SingleCardScreenData
import com.hatfat.cards.data.card.SingleCardScreenDataProvider
import com.hatfat.cards.util.CardRotationTransformation
import com.hatfat.trek1.R
import com.hatfat.trek1.repo.Trek1CardRepository
import com.hatfat.trek1.search.Trek1SearchResults
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class Trek1SingleCardScreenDataProvider @Inject constructor(
    private val cardRepo: Trek1CardRepository,
    @ApplicationContext val context: Context,
) : SingleCardScreenDataProvider {

    private val cardRotationTransformation = CardRotationTransformation()

    override fun getSingleCardScreenDataFromCard(cardData: SingleCardData): SingleCardScreenData {
        (cardData.searchResults as Trek1SearchResults).also {
            val cardId = it.getResult(cardData.position)
            val card = cardRepo.cardsMap.value?.get(cardId)

            return SingleCardScreenData { imageView: ImageView ->
                val placeholderResourceId = R.drawable.cardback

                val imageUrl = card?.frontImageUrl

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

package com.hatfat.cards.results.fullscreen

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.navArgs
import com.hatfat.cards.R
import com.hatfat.cards.results.general.SearchResultsCardData
import com.hatfat.cards.results.general.SearchResultsDataProvider
import com.hatfat.cards.glide.CardImageLoader
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class FullscreenCardActivity : AppCompatActivity() {

    @Inject
    lateinit var cardDataProvider: SearchResultsDataProvider

    @Inject
    lateinit var cardImageLoader: CardImageLoader

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val viewModel: FullscreenCardViewModel by viewModels()
        val args = navArgs<FullscreenCardActivityArgs>().value

        viewModel.setFullscreenCardState(args.fullscreenCardState)

        setContentView(R.layout.activity_fullscreen)

        viewModel.singleCardState.observe(this) { cardState ->
            val cardData = SearchResultsCardData()
            cardDataProvider.getCardDataForPosition(
                cardState.searchResults,
                cardState.position,
                cardData
            )

            findViewById<ImageView>(R.id.fullscreen_card_imageview)?.also {
                if (cardState.isRotated) {
                    it.rotation = 180.0f
                }

                if (cardState.isFlipped) {
                    if (cardData.hasDifferentBack) {
                        cardImageLoader.loadCardImageUrl(
                            cardData.backImageUrl,
                            it,
                            cardData.cardBackResourceId
                        )
                    } else {
                        cardImageLoader.loadCardResourceId(cardData.cardBackResourceId, it)
                    }
                } else {
                    cardImageLoader.loadCardImageUrl(
                        cardData.frontImageUrl,
                        it,
                        cardData.cardBackResourceId
                    )
                }
            }
        }

        findViewById<View>(R.id.fullscreen_card_frame_layout)?.also {
            it.setOnClickListener {
                onBackPressedDispatcher.onBackPressed()
            }
        }
    }
}

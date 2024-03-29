package com.hatfat.cards.results.fullscreen

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.navArgs
import com.hatfat.cards.R
import com.hatfat.cards.glide.CardImageLoader
import com.hatfat.cards.results.SearchResultsRepository
import com.hatfat.cards.results.general.SearchResultsCardData
import com.hatfat.cards.results.general.SearchResultsDataProvider
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import javax.inject.Named

@AndroidEntryPoint
class FullscreenCardActivity : AppCompatActivity() {

    @Inject
    lateinit var cardDataProvider: SearchResultsDataProvider

    @Inject
    @Named("StandardCardImageLoader")
    lateinit var cardImageLoader: CardImageLoader

    @Inject
    lateinit var searchResultsRepository: SearchResultsRepository

    private val viewModel: FullscreenCardViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val args = navArgs<FullscreenCardActivityArgs>().value

        viewModel.setFullscreenCardState(args.fullscreenCardState)

        setContentView(R.layout.activity_fullscreen)

        viewModel.singleCardState.observe(this) { cardState ->
            val cardData = SearchResultsCardData()

            searchResultsRepository.loadSearchResults(cardState.searchResultsKey)
                ?.let { searchResults ->
                    cardDataProvider.getCardDataForPosition(
                        searchResults,
                        cardState.position,
                        cardData
                    )
                }

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

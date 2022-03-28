package com.hatfat.cards.fullscreen

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.ImageView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.hatfat.cards.R
import com.hatfat.cards.data.card.SingleCardData
import com.hatfat.cards.data.card.SingleCardScreenDataProvider
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class FullscreenCardActivity : AppCompatActivity() {

    @Inject
    lateinit var cardScreenDataProvider: SingleCardScreenDataProvider

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val viewModel: FullscreenCardViewModel by viewModels()

        /* get our cardData from the intent */
        val intentCardData =
            intent.getSerializableExtra(SINGLE_CARD_DATA_EXTRA_KEY) as SingleCardData?

        intentCardData?.let {
            viewModel.setCardData(it)
        }

        setContentView(R.layout.activity_fullscreen)

        viewModel.fullscreenCard.observe(this) { cardData ->
            val cardScreenData = cardScreenDataProvider.getSingleCardScreenDataFromCard(cardData)

            findViewById<ImageView>(R.id.fullscreen_card_imageview)?.also {
                if (cardData.isRotated) {
                    it.rotation = 180.0f
                }

                cardScreenData.cardImageLoader(it)
            }
        }

        findViewById<View>(R.id.fullscreen_card_framelayout)?.also {
            it.setOnClickListener {
                onBackPressed()
            }
        }

        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
            window.insetsController?.hide(WindowInsets.Type.navigationBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
    }

    companion object {
        private const val SINGLE_CARD_DATA_EXTRA_KEY = "SINGLE_CARD_DATA_EXTRA_KEY"

        @JvmStatic
        fun intentForSingleCard(context: Context, cardData: SingleCardData): Intent {
            val intent = Intent(context, FullscreenCardActivity::class.java)
            intent.putExtra(SINGLE_CARD_DATA_EXTRA_KEY, cardData)
            return intent
        }
    }
}

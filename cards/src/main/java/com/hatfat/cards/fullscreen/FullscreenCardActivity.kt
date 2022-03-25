package com.hatfat.cards.fullscreen

import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.ImageView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.navArgs
import com.hatfat.cards.R
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
        val args = navArgs<FullscreenCardActivityArgs>().value

        viewModel.setCardData(args.cardData)

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
}

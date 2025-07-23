package com.hatfat.cards.about

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.insets.ColorProtection
import androidx.core.view.insets.ProtectionLayout
import androidx.core.view.updatePadding
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hatfat.cards.R
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class AboutActivity : AppCompatActivity() {

    @Inject
    lateinit var aboutCardsAdapter: AboutCardsListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.enableEdgeToEdge(window)

        setContentView(R.layout.activity_about)

        findViewById<ProtectionLayout>(R.id.protection_layout)
            .setProtections(
                listOf(
                    ColorProtection(
                        WindowInsetsCompat.Side.BOTTOM,
                        baseContext.resources.getColor(R.color.colorError)
                    ),
                    ColorProtection(
                        WindowInsetsCompat.Side.TOP,
                        baseContext.resources.getColor(R.color.colorPrimaryVariant)
                    ),
                )
            )

        // handle edge to edge layout, and inset our root view based on the phone system bars and display cutout values.
        val rootView = findViewById<View>(R.id.root_view)
        ViewCompat.setOnApplyWindowInsetsListener(rootView) { v, insets ->
            val bars = insets.getInsets(
                WindowInsetsCompat.Type.systemBars() or WindowInsetsCompat.Type.displayCutout()
            )

            v.updatePadding(
                left = bars.left,
                top = bars.top,
                right = bars.right,
                bottom = bars.bottom
            )

            WindowInsetsCompat.CONSUMED
        }

        findViewById<RecyclerView>(R.id.about_recyclerview).apply {
            this.layoutManager = LinearLayoutManager(context)
            this.adapter = aboutCardsAdapter
        }
    }
}
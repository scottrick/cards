package com.hatfat.cards.tabs

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.Window
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.insets.ColorProtection
import androidx.core.view.insets.GradientProtection
import androidx.core.view.insets.ProtectionLayout
import androidx.core.view.updatePadding
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.hatfat.cards.R
import com.hatfat.cards.about.AboutActivity
import com.hatfat.cards.results.SearchResultsRepository
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class CardsActivity : AppCompatActivity() {

    @Inject
    lateinit var searchResultsRepository: SearchResultsRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.enableEdgeToEdge(window)

        val viewModel: CardsActivityViewModel by viewModels()

        setContentView(R.layout.activity_cards)

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

        val toolbar = findViewById<Toolbar>(R.id.cards_toolbar)
        @Suppress("DEPRECATION")
        toolbar.overflowIcon?.setTint(resources.getColor(R.color.colorOnPrimary))
        setSupportActionBar(toolbar)

        val viewPager = findViewById<ViewPager2>(R.id.view_pager)
        val tabLayout = findViewById<TabLayout>(R.id.tab_layout)
        tabLayout.tabMode = TabLayout.MODE_FIXED
        val adapter = CardsFragmentAdapter(this, tabLayout, viewModel)
        viewPager.isUserInputEnabled = false //don't allow swiping

        viewModel.tabs.observe(this) {
            adapter.setNewTabs(it)
        }

        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)

                /* need to forward onPageSelected to the viewModel */
                tabLayout.getTabAt(position)?.let {
                    viewModel.onPageSelected(it, adapter)
                }
            }
        })

        val layoutMediator = TabLayoutMediator(tabLayout, viewPager, adapter)

        tabLayout.addOnTabSelectedListener(adapter)
        viewPager.adapter = adapter
        layoutMediator.attach()
    }

    override fun onStart() {
        super.onStart()
        searchResultsRepository.cleanUpOldSearchResults()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.cards_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.about_app -> {
                val myIntent = Intent(
                    this,
                    AboutActivity::class.java
                )
                this.startActivity(myIntent)
                true
            }

            else -> {
                super.onOptionsItemSelected(item)
            }
        }
    }
}
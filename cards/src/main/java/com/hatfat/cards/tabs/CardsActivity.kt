package com.hatfat.cards.tabs

import android.R.attr.value
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.hatfat.cards.R
import com.hatfat.cards.about.AboutActivity
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class CardsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val viewModel: CardsActivityViewModel by viewModels()

        setContentView(R.layout.activity_cards)

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

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.cards_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.about_app -> {
                val myIntent: Intent = Intent(
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
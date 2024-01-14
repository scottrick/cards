package com.hatfat.cards.tabs

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.hatfat.cards.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CardsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val viewModel: CardsActivityViewModel by viewModels()

        setContentView(R.layout.activity_cards)

        val toolbar = findViewById<Toolbar>(R.id.cards_toolbar)
        val viewPager = findViewById<ViewPager2>(R.id.view_pager)
        val tabLayout = findViewById<TabLayout>(R.id.tab_layout)
        tabLayout.tabMode = TabLayout.MODE_FIXED
        val adapter = CardsFragmentAdapter(this, tabLayout, viewModel)
        viewPager.isUserInputEnabled = false //don't allow swiping

        setSupportActionBar(toolbar)

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
}
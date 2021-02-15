package com.hatfat.cards.app

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.hatfat.cards.R
import com.hatfat.cards.tabs.CardFragmentAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CardsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_cards)

        val viewPager = findViewById<ViewPager2>(R.id.view_pager)
        val tabLayout = findViewById<TabLayout>(R.id.tab_layout)
        tabLayout.tabMode = TabLayout.MODE_FIXED
        val adapter = CardFragmentAdapter(this, tabLayout)
        viewPager.isUserInputEnabled = false //don't allow swiping

        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)

                /* need to forward onPageSelected to the adapter */
                tabLayout.getTabAt(position)?.let {
                    adapter.onPageSelected(it)
                }
            }
        })

        val layoutMediator = TabLayoutMediator(tabLayout, viewPager, adapter)

        tabLayout.addOnTabSelectedListener(adapter)
        viewPager.adapter = adapter
        layoutMediator.attach()
    }
}
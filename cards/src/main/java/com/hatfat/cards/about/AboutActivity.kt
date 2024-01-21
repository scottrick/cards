package com.hatfat.cards.about

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
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

        setContentView(R.layout.activity_about)

        findViewById<RecyclerView>(R.id.about_recyclerview).apply {
            this.layoutManager = LinearLayoutManager(context)
            this.adapter = aboutCardsAdapter
        }
    }
}
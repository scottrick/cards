package com.hatfat.cards.temp

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hatfat.cards.R
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class AnotherTestActivity : AppCompatActivity() {

    @Inject
    lateinit var testInterface: InterfaceForTesting

    @Inject
    lateinit var testListInterface: TestListInterface

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.test)

        val testTextView = findViewById<TextView>(R.id.testTextView)
        val testRecyclerView = findViewById<RecyclerView>(R.id.testRecyclerView)
        testTextView.text = testInterface.testString()

        testRecyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        val stringAdapter = TestStringAdapter()
        testRecyclerView.adapter = stringAdapter

        testListInterface.stringsLiveData.observe(this) {
            stringAdapter.updateStringList(it)
        }
    }
}


package com.hatfat.cards

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class AnotherTestActivity : AppCompatActivity() {

    @Inject
    lateinit var testInterface: InterfaceForTesting

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.test)

        var testTextView = findViewById<TextView>(R.id.testTextView)
        testTextView.text = testInterface.testString()
    }
}


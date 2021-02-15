package com.hatfat.cards.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.hatfat.cards.R

class CardsSearchFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_cards_search, container, false)
        val button = view.findViewById<Button>(R.id.test_button)

        button.setOnClickListener {
            val directions = CardsSearchFragmentDirections.actionCardSearchFragmentToCardListFragment()
            findNavController().navigate(directions)
        }

        return view
    }
}
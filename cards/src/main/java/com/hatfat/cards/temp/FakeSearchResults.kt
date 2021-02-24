package com.hatfat.cards.temp

import com.hatfat.cards.results.SearchResults

class FakeSearchResults(
    private val fakeResults: List<Int>
) : SearchResults() {

    override val size: Int = fakeResults.size

    override fun getResult(position: Int): Any {
        return fakeResults[position]
    }
}
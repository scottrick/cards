package com.hatfat.cards.results

import java.io.Serializable

abstract class SearchResults : Serializable {
    abstract val size: Int
    abstract fun getResult(position: Int): Any
}
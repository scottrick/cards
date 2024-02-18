package com.hatfat.cards.data

import java.io.Serializable

abstract class SearchResults : Serializable {
    abstract val size: Int
    abstract fun getResult(position: Int): Any
}
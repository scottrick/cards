package com.hatfat.trek1.search.filter

import com.hatfat.trek1.data.Trek1Card

interface Trek1Filter {
    fun filter(card: Trek1Card): Boolean
}
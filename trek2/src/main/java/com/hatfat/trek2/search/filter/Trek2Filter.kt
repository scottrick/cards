package com.hatfat.trek2.search.filter

import com.hatfat.trek2.data.Trek2Card

interface Trek2Filter {
    fun filter(card: Trek2Card): Boolean
}
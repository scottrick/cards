package com.hatfat.trek2.search.filter

import com.hatfat.trek2.data.Trek2Card
import com.hatfat.trek2.repo.Trek2SetRepository

interface Trek2Filter {
    fun filter(card: Trek2Card, setRepository: Trek2SetRepository): Boolean
}
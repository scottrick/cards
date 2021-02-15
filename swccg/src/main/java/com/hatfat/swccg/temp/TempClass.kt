package com.hatfat.swccg.temp

import com.hatfat.cards.temp.InterfaceForTesting
import javax.inject.Inject

class TempClass @Inject constructor() : InterfaceForTesting {
    override fun testString(): String {
        return "SWCCG TempClass String"
    }
}

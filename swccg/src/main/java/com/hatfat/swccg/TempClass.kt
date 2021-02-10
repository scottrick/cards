package com.hatfat.swccg

import com.hatfat.cards.InterfaceForTesting
import javax.inject.Inject

class TempClass @Inject constructor() : InterfaceForTesting {
    override fun testString(): String {
        return "SWCCG TempClass String"
    }
}

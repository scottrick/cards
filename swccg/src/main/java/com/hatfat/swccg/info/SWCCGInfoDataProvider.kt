package com.hatfat.swccg.info

import com.hatfat.cards.info.InfoDataProvider
import com.hatfat.cards.info.InfoScreenData
import com.hatfat.cards.info.InfoSelection
import javax.inject.Inject

class SWCCGInfoDataProvider @Inject constructor() : InfoDataProvider {
    override fun getInfoScreenDataFromSelection(selection: InfoSelection): InfoScreenData {
        return InfoScreenData(
            "TEST TITLE",
            "asdf",
            "zxcvzcv",
            listOf("sadfasdf", "sadfasdf", "asdfasdfwerwer", "123456")
        )
    }
}
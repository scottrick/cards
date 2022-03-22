package com.hatfat.cards.info

import javax.inject.Inject

class NotImplementedInfoDataProvider @Inject constructor() : InfoDataProvider {
    override fun getInfoScreenDataFromSelection(selection: InfoSelection): InfoScreenData {
        throw RuntimeException("Not Implemented!")
    }
}

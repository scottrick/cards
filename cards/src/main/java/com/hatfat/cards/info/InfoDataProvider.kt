package com.hatfat.cards.info

interface InfoDataProvider {
    fun getInfoScreenDataFromSelection(selection: InfoSelection): InfoScreenData
}
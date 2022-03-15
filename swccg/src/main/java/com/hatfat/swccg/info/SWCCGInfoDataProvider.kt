package com.hatfat.swccg.info

import com.hatfat.cards.info.InfoDataProvider
import com.hatfat.cards.info.InfoScreenData
import com.hatfat.cards.info.InfoSelection
import com.hatfat.swccg.repo.SWCCGCardRepository
import com.hatfat.swccg.search.SWCCGSearchResults
import javax.inject.Inject

class SWCCGInfoDataProvider @Inject constructor(
    val cardRepo: SWCCGCardRepository
) : InfoDataProvider {
    override fun getInfoScreenDataFromSelection(selection: InfoSelection): InfoScreenData {
        (selection.searchResults as SWCCGSearchResults).also {
            val cardId = it.getResult(selection.position)
            val card = cardRepo.cardsMap.value?.get(cardId)

            return InfoScreenData(
                card?.front?.title ?: "Unknown",
                "asdf",
                "zxcvzcv",
                card?.rulings ?: emptyList()
            )
        }
    }
}
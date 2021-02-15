package com.hatfat.swccg.repo

import android.content.res.Resources
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.hatfat.swccg.R
import com.hatfat.swccg.data.SWCCGCard
import com.hatfat.swccg.data.SWCCGCardIdList
import com.hatfat.swccg.data.SWCCGCardList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.BufferedReader
import java.io.InputStreamReader
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SWCCGCardRepository @Inject constructor(
    private val resources: Resources,
    private val gson: Gson
) {
    private val cardHashMapLiveData = MutableLiveData<Map<Int, SWCCGCard>>()
    private val sortedCardArrayLiveData = MutableLiveData<Array<SWCCGCard>>()
    private val sortedCardIdsListLiveData = MutableLiveData<SWCCGCardIdList>()

    val cardsMap: LiveData<Map<Int, SWCCGCard>>
        get() = cardHashMapLiveData

    val sortedCardsArray: LiveData<Array<SWCCGCard>>
        get() = sortedCardArrayLiveData

    val sortedCardIds: LiveData<SWCCGCardIdList>
        get() = sortedCardIdsListLiveData

    init {
        cardHashMapLiveData.value = HashMap()

        GlobalScope.launch(Dispatchers.IO) {
            load()
        }
    }

    private suspend fun load() {
        val hashMap = HashMap<Int, SWCCGCard>()
        val cardResources = arrayOf(R.raw.light, R.raw.dark)

        for (resource in cardResources) {
            val inputStream = resources.openRawResource(resource)
            val reader = BufferedReader(InputStreamReader(inputStream))
            val cardList = gson.fromJson(reader, SWCCGCardList::class.java)

            for (card in cardList.cards) {
                card.id?.let {
                    if (card.legacy == false) {
                        /* filter out legacy cards */
                        hashMap.put(it, card)
                    }
                }
            }
        }

        val array = hashMap.values.toTypedArray()
        array.sort()

        withContext(Dispatchers.Main) {
            cardHashMapLiveData.value = hashMap
            sortedCardArrayLiveData.value = array
            sortedCardIdsListLiveData.value = SWCCGCardIdList(array.mapNotNull { it.id })
        }
    }
}

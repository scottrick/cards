package com.hatfat.swccg.repo

import android.content.res.Resources
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.hatfat.cards.data.CardsRepository
import com.hatfat.swccg.R
import com.hatfat.swccg.data.SWCCGCard
import com.hatfat.swccg.data.SWCCGCardIdList
import com.hatfat.swccg.data.SWCCGCardList
import com.hatfat.swccg.service.GithubSwccgpcService
import kotlinx.coroutines.*
import java.io.BufferedReader
import java.io.InputStreamReader
import javax.inject.Inject
import javax.inject.Singleton

@DelicateCoroutinesApi
@Singleton
class SWCCGCardRepository @Inject constructor(
    private val swccgpcService: GithubSwccgpcService,
    private val resources: Resources,
    private val gson: Gson
) : CardsRepository() {
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

        var darkCardList = SWCCGCardList(emptyList())
        var lightCardList = SWCCGCardList(emptyList())

        try {
            darkCardList = swccgpcService.getDarkSideJson()
        } catch (e: Exception) {
            Log.e(TAG, "Error loading dark side cards from network: $e")
        }

        try {
            lightCardList = swccgpcService.getLightSideJson()
        } catch (e: Exception) {
            Log.e(TAG, "Error loading light side cards from network: $e")
        }

        if (darkCardList.cards.isEmpty()) {
            /* failed to load from network/cache, load backup from disk */
            try {
                val inputStream = resources.openRawResource(R.raw.dark)
                val reader = BufferedReader(InputStreamReader(inputStream))
                darkCardList = gson.fromJson(reader, SWCCGCardList::class.java)
            } catch (e: Exception) {
                Log.e(TAG, "Error loading dark side cards from disk: $e")
            }
        }

        if (lightCardList.cards.isEmpty()) {
            /* failed to load from network/cache, load backup from disk */
            try {
                val inputStream = resources.openRawResource(R.raw.light)
                val reader = BufferedReader(InputStreamReader(inputStream))
                lightCardList = gson.fromJson(reader, SWCCGCardList::class.java)
            } catch (e: Exception) {
                Log.e(TAG, "Error loading light side cards from disk: $e")
            }
        }

        val cardLists = listOf(darkCardList, lightCardList)
        cardLists.forEach { cardList ->
            cardList.cards.forEach { card ->
                card.id?.let {
                    if (card.legacy != true) {
                        /*
                            Only filter out cards that are explicitly marked as legacy.
                            Shouldn't be any since legacy cards are in their own json file now.
                         */
                        hashMap[it] = card
                    }
                }
            }
        }

        Log.i(TAG, "Loaded ${hashMap.values.size} cards total.")

        val array = hashMap.values.toTypedArray()
        array.sort()

        withContext(Dispatchers.Main) {
            cardHashMapLiveData.value = hashMap
            sortedCardArrayLiveData.value = array
            sortedCardIdsListLiveData.value = SWCCGCardIdList(array.mapNotNull { it.id })
            loadedLiveData.value = true
        }
    }

    companion object {
        private val TAG = SWCCGCardRepository::class.java.simpleName
    }
}

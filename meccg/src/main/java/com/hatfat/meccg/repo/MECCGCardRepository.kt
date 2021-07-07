package com.hatfat.meccg.repo

import android.content.res.Resources
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.hatfat.cards.data.CardsRepository
import com.hatfat.meccg.R
import com.hatfat.meccg.data.MECCGCard
import com.hatfat.meccg.service.GithubCardnumService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.BufferedReader
import java.io.InputStreamReader
import java.lang.reflect.Type
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MECCGCardRepository @Inject constructor(
    private val cardnumService: GithubCardnumService,
    private val resources: Resources,
    private val gson: Gson
) : CardsRepository() {
    private val cardHashMapLiveData = MutableLiveData<Map<String, MECCGCard>>()
    private val sortedCardArrayLiveData = MutableLiveData<Array<MECCGCard>>()
    private val sortedCardIdsListLiveData = MutableLiveData<List<String>>()

    val cardsMap: LiveData<Map<String, MECCGCard>>
        get() = cardHashMapLiveData

    val sortedCardsArray: LiveData<Array<MECCGCard>>
        get() = sortedCardArrayLiveData

    val sortedCardIds: LiveData<List<String>>
        get() = sortedCardIdsListLiveData

    init {
        cardHashMapLiveData.value = HashMap()

        GlobalScope.launch(Dispatchers.IO) {
            load()
        }
    }

    private suspend fun load() {
        val hashMap = HashMap<String, MECCGCard>()

        var cardList = emptyList<MECCGCard>()

        try {
            cardList = cardnumService.getCards()
        } catch (e: Exception) {
            Log.e(TAG, "Error loading meccg cards from network: $e")
        }

        if (cardList.isEmpty()) {
            /* failed to load from network/cache, load backup from disk */
            try {
                val meccgCardListType: Type = object : TypeToken<List<MECCGCard>>() {}.type
                val inputStream = resources.openRawResource(R.raw.cards_dc)
                val reader = BufferedReader(InputStreamReader(inputStream))
                cardList = gson.fromJson(reader, meccgCardListType)
            } catch (e: Exception) {
                Log.e(TAG, "Error loading meccg side cards from disk: $e")
            }
        }

        /* filter out unreleased cards. */
        cardList = cardList.filter { it.released == true }

        /* filter out The Wizards Unlimited cards */
        cardList = cardList.filter { it.set != "MEUL" }

        var nextId = 0

        /* give every card a unique ID and add it to the hashmap */
        cardList.forEach { card ->
            nextId += 1
            card.id = nextId.toString()

            hashMap[card.id] = card
        }

        Log.i(TAG, "Loaded ${hashMap.values.size} cards total.")

        val array = hashMap.values.toTypedArray()
        array.sort()

        withContext(Dispatchers.Main) {
            cardHashMapLiveData.value = hashMap
            sortedCardArrayLiveData.value = array
            sortedCardIdsListLiveData.value = array.map { it.id }
            loadedLiveData.value = true
        }
    }

    companion object {
        private val TAG = MECCGCardRepository::class.java.simpleName
    }
}
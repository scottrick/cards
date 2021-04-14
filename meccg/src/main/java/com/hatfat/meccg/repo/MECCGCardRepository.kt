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
import com.hatfat.meccg.service.GithubRezwitsService
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
    private val rezwitsService: GithubRezwitsService,
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
            cardList = rezwitsService.getCards()
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
                Log.e(TAG, "Error loading dark side cards from disk: $e")
            }
        }

        /* filter out unreleased cards and dreamcards. */
        /* The dreamcards have very inconsistent data which causes problems.  Incorrect IDs, missing data, etc */
        cardList = cardList.filter { it.released == true && it.dreamcard == false }

        cardList.forEach { card ->
            card.id?.let {
                if (!hashMap.containsKey(it)) {
                    hashMap[it] = card
                } else {
                    if (card.set != "MEUL") {
                        Log.e(TAG, "Found duplicate card which is already added, and wasn't from Wizards Unlimited: ${card.normalizedTitle} from ${card.set}")
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
            sortedCardIdsListLiveData.value = array.mapNotNull { it.id }
            loadedLiveData.value = true
        }
    }

    companion object {
        private val TAG = MECCGCardRepository::class.java.simpleName
    }
}
package com.hatfat.trek2.repo

import android.content.res.Resources
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.hatfat.cards.data.CardsRepository
import com.hatfat.trek2.R
import com.hatfat.trek2.data.Trek2Card
import com.hatfat.trek2.service.GithubEberlemsService
import com.hatfat.trek2.service.Trek2CardListAdapter
import kotlinx.coroutines.*
import javax.inject.Inject
import javax.inject.Singleton

@DelicateCoroutinesApi
@Singleton
class Trek2CardRepository @Inject constructor(
    private val eberlemsService: GithubEberlemsService,
    private val trek2CardListAdapter: Trek2CardListAdapter,
    private val resources: Resources
) : CardsRepository() {
    private val cardHashMapLiveData = MutableLiveData<Map<Int, Trek2Card>>()
    private val sortedCardArrayLiveData = MutableLiveData<Array<Trek2Card>>()
    private val sortedCardIdsListLiveData = MutableLiveData<List<Int>>()

    val cardsMap: LiveData<Map<Int, Trek2Card>>
        get() = cardHashMapLiveData

    val sortedCardsArray: LiveData<Array<Trek2Card>>
        get() = sortedCardArrayLiveData

    val sortedCardIds: LiveData<List<Int>>
        get() = sortedCardIdsListLiveData

    init {
        cardHashMapLiveData.value = HashMap()

        GlobalScope.launch(Dispatchers.IO) {
            load()
        }
    }

    private suspend fun load() {
        val hashMap = HashMap<Int, Trek2Card>()

        var physicalCardList: List<Trek2Card> = emptyList()
        var virtualCardList: List<Trek2Card> = emptyList()

        try {
            val responseBody = eberlemsService.getPhysicalCards()
            physicalCardList = trek2CardListAdapter.convert(responseBody.byteStream())
        } catch (e: Exception) {
            Log.e(TAG, "Error loading physical trek2 cards from network: $e")
        }

        if (physicalCardList.isEmpty()) {
            /* failed to load from network/cache, load backup from disk */
            try {
                val physicalInputStream = resources.openRawResource(R.raw.physical)
                physicalCardList = trek2CardListAdapter.convert(physicalInputStream)
            } catch (e: Exception) {
                Log.e(TAG, "Error loading physical trek2 cards from disk: $e")
            }
        }

        try {
            val responseBody = eberlemsService.getVirtualCards()
            virtualCardList = trek2CardListAdapter.convert(responseBody.byteStream())
        } catch (e: Exception) {
            Log.e(TAG, "Error loading virtual trek2 cards from network: $e")
        }

        if (virtualCardList.isEmpty()) {
            /* failed to load from network/cache, load backup from disk */
            try {
                val virtualInputStream = resources.openRawResource(R.raw.virtual)
                virtualCardList = trek2CardListAdapter.convert(virtualInputStream)
            } catch (e: Exception) {
                Log.e(TAG, "Error loading virtual trek2 cards from disk: $e")
            }
        }

        val allCards = listOf(physicalCardList, virtualCardList)

        Log.i(TAG, "Loaded ${physicalCardList.size} physical cards total.")
        Log.i(TAG, "Loaded ${virtualCardList.size} virtual cards total.")

        allCards.forEach { cardList ->
            cardList.forEach { card ->
                card.id.let {
                    hashMap[it] = card
                }
            }
        }

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
        private val TAG = Trek2CardRepository::class.java.simpleName
    }
}
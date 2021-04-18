package com.hatfat.trek1.repo

import android.content.res.Resources
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.hatfat.cards.data.CardsRepository
import com.hatfat.trek1.R
import com.hatfat.trek1.data.Trek1Card
import com.hatfat.trek1.service.GithubEberlemsService
import com.hatfat.trek1.service.Trek1CardListAdapter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class Trek1CardRepository @Inject constructor(
    private val eberlemsService: GithubEberlemsService,
    private val trek1CardListAdapter: Trek1CardListAdapter,
    private val resources: Resources
) : CardsRepository() {
    private val cardHashMapLiveData = MutableLiveData<Map<String, Trek1Card>>()
    private val sortedCardArrayLiveData = MutableLiveData<Array<Trek1Card>>()
    private val sortedCardIdsListLiveData = MutableLiveData<List<String>>()

    val cardsMap: LiveData<Map<String, Trek1Card>>
        get() = cardHashMapLiveData

    val sortedCardsArray: LiveData<Array<Trek1Card>>
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
        val hashMap = HashMap<String, Trek1Card>()

        var physicalCardList: List<Trek1Card> = emptyList()
        var virtualCardList: List<Trek1Card> = emptyList()

        try {
            val responseBody = eberlemsService.getPhysicalCards()
            physicalCardList = trek1CardListAdapter.convert(responseBody.byteStream())
        } catch (e: Exception) {
            Log.e(TAG, "Error loading physical trek1 cards from network: $e")
        }

        if (physicalCardList.isEmpty()) {
            Log.e(TAG, "Loading from disk phys???")
            /* failed to load from network/cache, load backup from disk */
            try {
                val physicalInputStream = resources.openRawResource(R.raw.physical)
                physicalCardList = trek1CardListAdapter.convert(physicalInputStream)
            } catch (e: Exception) {
                Log.e(TAG, "Error loading physical trek1 cards from disk: $e")
            }
        }

        try {
            val responseBody = eberlemsService.getVirtualCards()
            virtualCardList = trek1CardListAdapter.convert(responseBody.byteStream())
        } catch (e: Exception) {
            Log.e(TAG, "Error loading virtual trek1 cards from network: $e")
        }

        if (virtualCardList.isEmpty()) {
            Log.e(TAG, "Loading from disk virt???")
            /* failed to load from network/cache, load backup from disk */
            try {
                val virtualInputStream = resources.openRawResource(R.raw.virtual)
                virtualCardList = trek1CardListAdapter.convert(virtualInputStream)
            } catch (e: Exception) {
                Log.e(TAG, "Error loading virtual trek1 cards from disk: $e")
            }
        }

        Log.i(TAG, "Loaded ${physicalCardList.size} physical cards total.")
        Log.i(TAG, "Loaded ${virtualCardList.size} virtual cards total.")

        val setsHashSet = HashSet<String>()

        physicalCardList = physicalCardList.filter {
            it.set != "ban_2E"
        }

        virtualCardList = virtualCardList.filter {
            it.set != "ban_2E"
        }

        val allCards = listOf(physicalCardList, virtualCardList)

        allCards.forEach { cardList ->
            cardList.forEach {
                it.set?.let {
                    setsHashSet.add(it)
                }
            }
        }

        Log.i(TAG, "Loaded ${physicalCardList.size} physical cards total.")
        Log.i(TAG, "Loaded ${virtualCardList.size} virtual cards total.")

        allCards.forEach { cardList ->
            cardList.forEach { card ->
                card.id?.let {
                    hashMap[it] = card
                }
            }
        }

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
        private val TAG = Trek1CardRepository::class.java.simpleName
    }
}

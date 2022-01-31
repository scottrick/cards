package com.hatfat.trek1.repo

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.reflect.TypeToken
import com.hatfat.cards.data.CardsRepository
import com.hatfat.cards.data.loader.DataDesc
import com.hatfat.cards.data.loader.DataLoader
import com.hatfat.trek1.R
import com.hatfat.trek1.data.Trek1Card
import com.hatfat.trek1.service.GithubEberlemsService
import com.hatfat.trek1.service.Trek1CardListAdapter
import kotlinx.coroutines.*
import javax.inject.Inject
import javax.inject.Singleton

@DelicateCoroutinesApi
@Singleton
class Trek1CardRepository @Inject constructor(
    private val eberlemsService: GithubEberlemsService,
    private val trek1CardListAdapter: Trek1CardListAdapter,
    private val dataLoader: DataLoader,
) : CardsRepository() {
    private val cardHashMapLiveData = MutableLiveData<Map<Int, Trek1Card>>()
    private val sortedCardArrayLiveData = MutableLiveData<Array<Trek1Card>>()
    private val sortedCardIdsListLiveData = MutableLiveData<List<Int>>()

    val cardsMap: LiveData<Map<Int, Trek1Card>>
        get() = cardHashMapLiveData

    val sortedCardsArray: LiveData<Array<Trek1Card>>
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
        val typeToken = object : TypeToken<List<Trek1Card>>() {}
        val physicalDataDesc = DataDesc(
            typeToken,
            {
                val responseBody = eberlemsService.getPhysicalCards()
                trek1CardListAdapter.convert(responseBody.byteStream())
            },
            R.raw.physical,
            emptyList(),
            "physical",
        )

        val virtualDataDesc = DataDesc(
            typeToken,
            {
                val responseBody = eberlemsService.getVirtualCards()
                trek1CardListAdapter.convert(responseBody.byteStream())
            },
            R.raw.virtual,
            emptyList(),
            "virtual",
        )

        val physicalCardList = dataLoader.load(physicalDataDesc)
        val virtualCardList = dataLoader.load(virtualDataDesc)

        val hashMap = HashMap<Int, Trek1Card>()
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
        private val TAG = Trek1CardRepository::class.java.simpleName
    }
}
package com.hatfat.trek2.repo

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.reflect.TypeToken
import com.hatfat.cards.data.CardsRepository
import com.hatfat.cards.data.loader.DataDesc
import com.hatfat.cards.data.loader.DataLoader
import com.hatfat.trek2.R
import com.hatfat.trek2.data.Trek2Card
import com.hatfat.trek2.service.GithubEberlemsService
import com.hatfat.trek2.service.Trek2CardListAdapter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class Trek2CardRepository @Inject constructor(
    private val eberlemsService: GithubEberlemsService,
    private val dataLoader: DataLoader,
) : CardsRepository() {
    private val physicalCardListAdapter = Trek2CardListAdapter(0)
    private val virtualCardListAdapter = Trek2CardListAdapter(10_000)

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
    }

    override suspend fun load() {
        val typeToken = object : TypeToken<List<Trek2Card>>() {}
        val physicalDataDesc = DataDesc(
            typeToken,
            {
                val responseBody = eberlemsService.getPhysicalCards()
                physicalCardListAdapter.convert(responseBody.byteStream())
            },
            R.raw.physical,
            emptyList(),
            "physical",
        )

        val virtualDataDesc = DataDesc(
            typeToken,
            {
                val responseBody = eberlemsService.getVirtualCards()
                virtualCardListAdapter.convert(responseBody.byteStream())
            },
            R.raw.virtual,
            emptyList(),
            "virtual",
        )

        val physicalCardList = dataLoader.load(physicalDataDesc)
        val virtualCardList = dataLoader.load(virtualDataDesc)

        val hashMap = HashMap<Int, Trek2Card>()
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
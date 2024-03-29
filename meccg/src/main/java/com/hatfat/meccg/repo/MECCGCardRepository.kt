package com.hatfat.meccg.repo

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.reflect.TypeToken
import com.hatfat.cards.data.CardsRepository
import com.hatfat.cards.data.loader.DataDesc
import com.hatfat.cards.data.loader.DataLoader
import com.hatfat.meccg.R
import com.hatfat.meccg.data.MECCGCard
import com.hatfat.meccg.service.GithubCardnumService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MECCGCardRepository @Inject constructor(
    private val cardnumService: GithubCardnumService,
    private val dataLoader: DataLoader,
) : CardsRepository() {
    private val cardHashMapLiveData = MutableLiveData<Map<Int, MECCGCard>>()
    private val sortedCardArrayLiveData = MutableLiveData<Array<MECCGCard>>()
    private val sortedCardIdsListLiveData = MutableLiveData<List<Int>>()

    val cardsMap: LiveData<Map<Int, MECCGCard>>
        get() = cardHashMapLiveData

    val sortedCardsArray: LiveData<Array<MECCGCard>>
        get() = sortedCardArrayLiveData

    val sortedCardIds: LiveData<List<Int>>
        get() = sortedCardIdsListLiveData

    init {
        cardHashMapLiveData.value = HashMap()
    }

    override suspend fun load() {
        val typeToken = object : TypeToken<List<MECCGCard>>() {}
        val dataDesc = DataDesc(
            typeToken,
            { cardnumService.getCards() },
            R.raw.cards_dc,
            emptyList(),
            "cards",
        )

        var cardList = dataLoader.load(dataDesc)
        val hashMap = HashMap<Int, MECCGCard>()

        /* filter out unreleased cards. */
        cardList = cardList.filter { it.released == true }

        /* filter out The Wizards Unlimited cards */
        cardList = cardList.filter { it.set != "MEUL" }

        var nextId = 0

        /* give every card a unique ID and add it to the hashmap */
        cardList.forEach { card ->
            nextId += 1
            card.id = nextId

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
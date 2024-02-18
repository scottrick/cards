package com.hatfat.swccg.repo

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.reflect.TypeToken
import com.hatfat.cards.data.CardsRepository
import com.hatfat.cards.data.loader.DataDesc
import com.hatfat.cards.data.loader.DataLoader
import com.hatfat.swccg.R
import com.hatfat.swccg.data.SWCCGCard
import com.hatfat.swccg.data.SWCCGCardIdList
import com.hatfat.swccg.data.SWCCGCardList
import com.hatfat.swccg.data.SWCCGPrinting
import com.hatfat.swccg.service.GithubSwccgpcService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SWCCGCardRepository @Inject constructor(
    private val swccgpcService: GithubSwccgpcService,
    private val dataLoader: DataLoader,
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
    }

    override suspend fun load() {
        val typeToken = object : TypeToken<SWCCGCardList>() {}

        val darkSideDataDesc = DataDesc(
            typeToken,
            { swccgpcService.getDarkSideJson() },
            R.raw.dark,
            SWCCGCardList(emptyList()),
            "dark",
        )

        val lightSideDataDesc = DataDesc(
            typeToken,
            { swccgpcService.getLightSideJson() },
            R.raw.light,
            SWCCGCardList(emptyList()),
            "light",
        )

        val darkSideLegacyDataDesc = DataDesc(
            typeToken,
            { swccgpcService.getDarkSideLegacyJson() },
            R.raw.dark,
            SWCCGCardList(emptyList()),
            "darkLegacy",
        )

        val lightSideLegacyDataDesc = DataDesc(
            typeToken,
            { swccgpcService.getLightSideLegacyJson() },
            R.raw.light,
            SWCCGCardList(emptyList()),
            "lightLegacy",
        )

        val results = mutableListOf<SWCCGCardList>()
        val hashMap = HashMap<Int, SWCCGCard>()

        val cardLoadingTasks = listOf(
            coroutineScope.async(coroutineDispatcher) {
                results.add(dataLoader.load(darkSideDataDesc))
            },
            coroutineScope.async(coroutineDispatcher) {
                results.add(dataLoader.load(lightSideDataDesc))
            },
            coroutineScope.async(coroutineDispatcher) {
                results.add(dataLoader.load(darkSideLegacyDataDesc))
            },
            coroutineScope.async(coroutineDispatcher) {
                results.add(dataLoader.load(lightSideLegacyDataDesc))
            },
        )

        /* wait for all cards to load */
        cardLoadingTasks.awaitAll()

        results.forEach { cardList ->
            cardList.cards.forEach { card ->
                card.id?.let {
                    if (card.legacy == true) {
                        /* Manually add a printing "601" to match how gemp is representing the Legacy set. */
                        val printings = card.printings?.toMutableSet() ?: mutableSetOf()
                        printings.add(SWCCGPrinting("601"))
                        card.printings = printings
                    }

                    hashMap[it] = card
                }
            }
        }

        Log.i(TAG, "Loaded ${hashMap.values.size} cards total.")

        val array = hashMap.values.toTypedArray()
        array.sort()

        withContext(Dispatchers.Main) {
            cardHashMapLiveData.value = hashMap
            sortedCardArrayLiveData.value = array
            sortedCardIdsListLiveData.value = SWCCGCardIdList(array.mapNotNull { it.id }.toIntArray())
            loadedLiveData.value = true
        }
    }

    companion object {
        private val TAG = SWCCGCardRepository::class.java.simpleName
    }
}

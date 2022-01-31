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
import com.hatfat.trek1.data.Trek1Set
import com.hatfat.trek1.service.GithubEberlemsService
import kotlinx.coroutines.*
import javax.inject.Inject
import javax.inject.Singleton

@DelicateCoroutinesApi
@Singleton
class Trek1SetRepository @Inject constructor(
    private val eberlemsService: GithubEberlemsService,
    private val dataLoader: DataLoader,
) : CardsRepository() {
    private val setListLiveData = MutableLiveData<List<Trek1Set>>()
    private val setMapLiveData = MutableLiveData<Map<String, Trek1Set>>()

    val setList: LiveData<List<Trek1Set>>
        get() = setListLiveData
    val setMap: LiveData<Map<String, Trek1Set>>
        get() = setMapLiveData

    private val secondEditionSet = Trek1Set(
        "2e",
        "2e",
        "2e",
        false
    )

    private val unknownSet = Trek1Set(
        "unknown",
        "Unknown",
        "unknown",
        false
    )

    init {
        setListLiveData.value = emptyList()
        setMapLiveData.value = HashMap()

        GlobalScope.launch(Dispatchers.IO) {
            load()
        }
    }

    fun getSetForCard(card: Trek1Card): Trek1Set {
        if (card.is2eCompatible) {
            return secondEditionSet
        }

        return setMap.value?.get(card.release ?: "") ?: unknownSet
    }

    private suspend fun load() {
        val typeToken = object : TypeToken<List<Trek1Set>>() {}
        val dataDesc = DataDesc(
            typeToken,
            { eberlemsService.getSets() },
            R.raw.sets,
            emptyList(),
            "sets",
        )

        val sets = dataLoader.load(dataDesc)
        val hashMap = HashMap<String, Trek1Set>()

        Log.i(TAG, "Loaded ${sets.size} sets.")

        sets.forEach { set ->
            hashMap[set.code] = set
        }

        withContext(Dispatchers.Main) {
            setListLiveData.value = sets
            setMapLiveData.value = hashMap
            loadedLiveData.value = true
        }
    }

    companion object {
        private val TAG = Trek1SetRepository::class.java.simpleName
    }
}
package com.hatfat.trek1.repo

import android.content.res.Resources
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.hatfat.cards.data.CardsRepository
import com.hatfat.trek1.R
import com.hatfat.trek1.data.Trek1Card
import com.hatfat.trek1.data.Trek1Set
import com.hatfat.trek1.service.GithubEberlemsService
import kotlinx.coroutines.*
import java.io.BufferedReader
import java.io.InputStreamReader
import java.lang.reflect.Type
import javax.inject.Inject
import javax.inject.Singleton

@DelicateCoroutinesApi
@Singleton
class Trek1SetRepository @Inject constructor(
    private val eberlemsService: GithubEberlemsService,
    private val resources: Resources,
    private val gson: Gson
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
        val hashMap = HashMap<String, Trek1Set>()

        var sets: List<Trek1Set> = emptyList()

        try {
            sets = eberlemsService.getSets()
        } catch (e: Exception) {
            Log.e(TAG, "Error loading trek1 sets from network: $e")
        }

        if (sets.isEmpty()) {
            /* failed to load from network/cache, load backup from disk */
            try {
                val setsInputStream = resources.openRawResource(R.raw.sets)
                val setsReader = BufferedReader(InputStreamReader(setsInputStream))
                val setListType: Type = object : TypeToken<List<Trek1Set>>() {}.type
                sets = gson.fromJson(setsReader, setListType)
            } catch (e: Exception) {
                Log.e(TAG, "Error loading sets from disk: $e")
            }
        }

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
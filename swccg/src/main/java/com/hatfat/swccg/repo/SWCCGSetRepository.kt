package com.hatfat.swccg.repo

import android.content.res.Resources
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.hatfat.cards.data.CardsRepository
import com.hatfat.swccg.R
import com.hatfat.swccg.data.SWCCGSet
import com.hatfat.swccg.service.GithubSwccgpcService
import kotlinx.coroutines.*
import java.io.BufferedReader
import java.io.InputStreamReader
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SWCCGSetRepository @Inject constructor(
    private val swccgService: GithubSwccgpcService,
    private val resources: Resources,
    private val gson: Gson
) : CardsRepository() {
    /* set ID --> Set */
    private val setMapLiveData = MutableLiveData<Map<String, SWCCGSet>>()
    val setMap: LiveData<Map<String, SWCCGSet>>
        get() = setMapLiveData

    private val setListLiveData = MutableLiveData<List<SWCCGSet>>()
    val setList: LiveData<List<SWCCGSet>>
        get() = setListLiveData

    init {
        setMapLiveData.value = HashMap()
        setListLiveData.value = mutableListOf()

        GlobalScope.launch(Dispatchers.IO) {
            load()
        }
    }

    private suspend fun load() {
        var sets: List<SWCCGSet> = emptyList()

        try {
            sets = swccgService.getSets()
        } catch (e: Exception) {
            Log.e(TAG, "Error loading sets: $e")
        }

        if (sets.isEmpty()) {
            /* no sets downloaded, load our backup from disk */
            try {
                val inputStream = resources.openRawResource(R.raw.sets)
                val reader = BufferedReader(InputStreamReader(inputStream))
                sets = gson.fromJson(reader, Array<SWCCGSet>::class.java).toList()
            } catch (e: Exception) {
                Log.e(TAG, "Error loading sets from disk: $e")
            }
        }

        /* remove dream set */
        sets = sets.filter { !it.id.contains(Regex("40.")) }
        /* remove playtesting set */
        sets = sets.filter { !it.id.contains(Regex("50.")) }

        val hashMap = HashMap<String, SWCCGSet>()
        for (set in sets) {
            hashMap[set.id] = set
        }

        withContext(Dispatchers.Main) {
            setMapLiveData.value = hashMap
            setListLiveData.value = sets
            loadedLiveData.value = true
        }
    }

    private companion object {
        private val TAG = SWCCGSetRepository::class.java.simpleName
    }
}

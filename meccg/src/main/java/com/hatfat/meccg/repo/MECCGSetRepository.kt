package com.hatfat.meccg.repo

import android.content.res.Resources
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.hatfat.cards.data.CardsRepository
import com.hatfat.meccg.R
import com.hatfat.meccg.data.MECCGSet
import com.hatfat.meccg.service.GithubCardnumService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.BufferedReader
import java.io.InputStreamReader
import javax.inject.Inject
import javax.inject.Named
import javax.inject.Singleton

@Suppress("unused")
@Singleton
class MECCGSetRepository @Inject constructor(
    @Named("should use dreamcards") private val shouldUseDreamcards: Boolean,
    private val cardnumService: GithubCardnumService,
    private val resources: Resources,
    private val gson: Gson
) : CardsRepository() {
    /* set code --> Set */
    private val setMapLiveData = MutableLiveData<Map<String, MECCGSet>>()
    val setMap: LiveData<Map<String, MECCGSet>>
        get() = setMapLiveData

    private val setListLiveData = MutableLiveData<List<MECCGSet>>()
    val setList: LiveData<List<MECCGSet>>
        get() = setListLiveData

    init {
        setMapLiveData.value = HashMap()
        setListLiveData.value = mutableListOf()

        GlobalScope.launch(Dispatchers.IO) {
            load()
        }
    }

    private suspend fun load() {
        var sets: List<MECCGSet> = emptyList()

        try {
            sets = cardnumService.getSets()
        } catch (e: Exception) {
            Log.e(TAG, "Error loading sets: $e")
        }

        if (sets.isEmpty()) {
            /* no sets downloaded, load our backup from disk */
            try {
                val inputStream = resources.openRawResource(R.raw.sets_dc)
                val reader = BufferedReader(InputStreamReader(inputStream))
                sets = gson.fromJson(reader, Array<MECCGSet>::class.java).toList()
            } catch (e: Exception) {
                Log.e(TAG, "Error loading sets from disk: $e")
            }
        }

        /* remove unreleased sets */
        sets = sets.filter { it.released == true }
        /* remove unlimited sets */
        sets = sets.filter { it.name?.contains("Unlimited") != true }

        if (!shouldUseDreamcards) {
            /* no dreamcards!  filter the DC sets */
            sets = sets.filter { it.dreamcards != true }
        }

        val hashMap = HashMap<String, MECCGSet>()
        for (set in sets) {
            set.code?.let { code ->
                hashMap[code] = set
            }
        }

        sets = sets.sortedBy { it.position }

        withContext(Dispatchers.Main) {
            setMapLiveData.value = hashMap
            setListLiveData.value = sets
            loadedLiveData.value = true
        }
    }

    private companion object {
        private val TAG = MECCGSetRepository::class.java.simpleName
    }
}
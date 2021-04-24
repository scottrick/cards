package com.hatfat.trek2.repo

import android.content.res.Resources
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.hatfat.cards.data.CardsRepository
import com.hatfat.trek2.R
import com.hatfat.trek2.data.Trek2Set
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.BufferedReader
import java.io.InputStreamReader
import java.lang.reflect.Type
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class Trek2SetRepository @Inject constructor(
    private val resources: Resources,
    private val gson: Gson
) : CardsRepository() {
    private val setListLiveData = MutableLiveData<List<Trek2Set>>()
    private val setMapLiveData = MutableLiveData<Map<String, Trek2Set>>()

    val setList: LiveData<List<Trek2Set>>
        get() = setListLiveData
    val setMap: LiveData<Map<String, Trek2Set>>
        get() = setMapLiveData

    init {
        setListLiveData.value = emptyList()
        setMapLiveData.value = HashMap()

        GlobalScope.launch(Dispatchers.IO) {
            load()
        }
    }

    private suspend fun load() {
        val hashMap = HashMap<String, Trek2Set>()

        var sets: List<Trek2Set> = emptyList()

        try {
            val setsInputStream = resources.openRawResource(R.raw.sets)
            val setsReader = BufferedReader(InputStreamReader(setsInputStream))
            val setListType: Type = object : TypeToken<List<Trek2Set>>() {}.type
            sets = gson.fromJson(setsReader, setListType)
        } catch (e: Exception) {
            Log.e(TAG, "Error loading sets from disk: $e")
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
        private val TAG = Trek2SetRepository::class.java.simpleName
    }
}

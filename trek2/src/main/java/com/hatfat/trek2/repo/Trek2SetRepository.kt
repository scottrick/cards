package com.hatfat.trek2.repo

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.reflect.TypeToken
import com.hatfat.cards.data.CardsRepository
import com.hatfat.cards.data.loader.DataDesc
import com.hatfat.cards.data.loader.DataLoader
import com.hatfat.trek2.R
import com.hatfat.trek2.data.Trek2Set
import com.hatfat.trek2.service.GithubEberlemsService
import kotlinx.coroutines.*
import javax.inject.Inject
import javax.inject.Singleton

@DelicateCoroutinesApi
@Singleton
class Trek2SetRepository @Inject constructor(
    private val eberlemsService: GithubEberlemsService,
    private val dataLoader: DataLoader,
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
        val typeToken = object : TypeToken<List<Trek2Set>>() {}
        val dataDesc = DataDesc(
            typeToken,
            { eberlemsService.getSets() },
            R.raw.sets,
            emptyList(),
            "sets",
        )

        val sets = dataLoader.load(dataDesc)
        val hashMap = HashMap<String, Trek2Set>()

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

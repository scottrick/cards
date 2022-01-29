package com.hatfat.swccg.repo

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.reflect.TypeToken
import com.hatfat.cards.data.CardsRepository
import com.hatfat.cards.data.loader.DataDesc
import com.hatfat.cards.data.loader.DataLoader
import com.hatfat.swccg.R
import com.hatfat.swccg.data.SWCCGSet
import com.hatfat.swccg.service.GithubSwccgpcService
import kotlinx.coroutines.*
import javax.inject.Inject
import javax.inject.Singleton

@DelicateCoroutinesApi
@Singleton
class SWCCGSetRepository @Inject constructor(
    private val swccgService: GithubSwccgpcService,
    private val dataLoader: DataLoader,
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
        val typeToken = object : TypeToken<List<SWCCGSet>>() {}
        val dataDesc = DataDesc(
            typeToken,
            { swccgService.getSets() },
            R.raw.sets,
            emptyList(),
            "sets",
        )

        var sets = dataLoader.load(dataDesc)

        /* remove dream set */
        sets = sets.filter { !it.id.contains(Regex("40.")) }
        /* remove play-testing set */
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
}

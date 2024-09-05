package com.hatfat.meccg.repo

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.reflect.TypeToken
import com.hatfat.cards.data.CardsRepository
import com.hatfat.cards.data.loader.DataDesc
import com.hatfat.cards.data.loader.DataLoader
import com.hatfat.meccg.R
import com.hatfat.meccg.data.MECCGSet
import com.hatfat.meccg.service.GithubCardnumService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Named
import javax.inject.Singleton

@Suppress("unused")
@Singleton
class MECCGSetRepository @Inject constructor(
    @Named("should use dreamcards") private val shouldUseDreamcards: Boolean,
    private val cardnumService: GithubCardnumService,
    private val dataLoader: DataLoader,
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
    }

    override suspend fun load() {
        val typeToken = object : TypeToken<List<MECCGSet>>() {}
        val dataDesc = DataDesc(
            typeToken,
            { cardnumService.getSets() },
            R.raw.sets_dc,
            emptyList(),
            "sets",
        )

        var sets = dataLoader.load(dataDesc)

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
package com.hatfat.swccg.repo

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.reflect.TypeToken
import com.hatfat.cards.data.CardsRepository
import com.hatfat.cards.data.loader.DataDesc
import com.hatfat.cards.data.loader.DataLoader
import com.hatfat.swccg.R
import com.hatfat.swccg.data.format.SWCCGFormat
import com.hatfat.swccg.service.GithubPlayersCommitteeService
import kotlinx.coroutines.*
import javax.inject.Inject
import javax.inject.Singleton

@DelicateCoroutinesApi
@Suppress("unused")
@Singleton
class SWCCGFormatRepository @Inject constructor(
    private val pcService: GithubPlayersCommitteeService,
    private val dataLoader: DataLoader,
) : CardsRepository() {
    /* format CODE --> format */
    private val formatsMapLiveData = MutableLiveData<Map<String, SWCCGFormat>>()
    val formatsMap: LiveData<Map<String, SWCCGFormat>>
        get() = formatsMapLiveData

    private val formatsListLiveData = MutableLiveData<List<SWCCGFormat>>()
    val formatsList: LiveData<List<SWCCGFormat>>
        get() = formatsListLiveData

    init {
        formatsMapLiveData.value = HashMap()
        formatsListLiveData.value = emptyList()

        GlobalScope.launch(Dispatchers.IO) {
            load()
        }
    }

    private suspend fun load() {
        val typeToken = object : TypeToken<List<SWCCGFormat>>() {}
        val dataDesc = DataDesc(
            typeToken,
            { pcService.getFormats() },
            R.raw.formats,
            emptyList(),
            "formats",
        )

        var formats = dataLoader.load(dataDesc)

        /* filter out formats we don't want to see in the formats list */
        formats = formats.filter {
            if (it.hall == false) {
                return@filter false
            }

            if (it.playtesting == true) {
                return@filter false
            }

            if (it.deckSize != null && it.deckSize != 60) {
                return@filter false
            }

            if (it.code == "dream_cards") {
                return@filter false
            }

            true
        }

        val hashMap = HashMap<String, SWCCGFormat>()
        for (format in formats) {
            hashMap[format.code] = format
        }

        withContext(Dispatchers.Main) {
            formatsMapLiveData.value = hashMap
            formatsListLiveData.value = formats
            loadedLiveData.value = true
        }
    }
}

package com.hatfat.swccg.repo

import android.content.res.Resources
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.hatfat.cards.data.CardsRepository
import com.hatfat.swccg.R
import com.hatfat.swccg.data.format.SWCCGFormat
import com.hatfat.swccg.service.GithubPlayersCommitteeService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.BufferedReader
import java.io.InputStreamReader
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SWCCGFormatRepository @Inject constructor(
    private val pcService: GithubPlayersCommitteeService,
    private val resources: Resources,
    private val gson: Gson
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
        var formats: List<SWCCGFormat> = emptyList()

        try {
            formats = pcService.getFormats()
        } catch (e: Exception) {
            Log.e(TAG, "Error loading formats: $e")
        }

        if (formats.isEmpty()) {
            /* no formats downloaded, load our backup from disk */
            try {
                val inputStream = resources.openRawResource(R.raw.formats)
                val reader = BufferedReader(InputStreamReader(inputStream))
                formats = gson.fromJson(reader, Array<SWCCGFormat>::class.java).toList()
            } catch (e: Exception) {
                Log.e(TAG, "Error loading formats from disk: $e")
            }
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

    private companion object {
        private val TAG = SWCCGFormatRepository::class.java.simpleName
    }
}

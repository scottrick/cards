package com.hatfat.swccg.repo

import android.content.res.Resources
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.hatfat.swccg.R
import com.hatfat.swccg.data.SWCCGSet
import com.hatfat.swccg.service.GithubSwccgpcService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.BufferedReader
import java.io.InputStreamReader
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SWCCGSetRepository @Inject constructor(
    private val swccgService: GithubSwccgpcService,
    private val resources: Resources,
    private val gson: Gson
) {
    /* set ID --> Set */
    private val setLiveData = MutableLiveData<Map<Int, SWCCGSet>>()
    val sets: LiveData<Map<Int, SWCCGSet>>
        get() = setLiveData

    init {
        setLiveData.value = HashMap()

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

        val hashMap = HashMap<Int, SWCCGSet>()
        for (set in sets) {
            hashMap[set.id] = set
        }

        withContext(Dispatchers.Main) {
            Log.e("catfat", "finished loading sets: ${hashMap.size}")
            setLiveData.value = hashMap
        }
    }

    private companion object {
        private val TAG = SWCCGSetRepository::class.java.simpleName
    }
}

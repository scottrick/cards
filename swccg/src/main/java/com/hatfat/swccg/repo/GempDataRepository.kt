package com.hatfat.swccg.repo

import android.content.res.Resources
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.hatfat.swccg.data.format.SWCCGFormat
import com.hatfat.swccg.service.GithubPlayersCommitteeService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.Reader
import javax.inject.Inject
import javax.inject.Singleton

/* catfat i think we can delete this eventually? */

@Singleton
class GempDataRepository @Inject constructor(
    private val pcService: GithubPlayersCommitteeService,
    private val resources: Resources,
    private val gson: Gson
) {
    //CATFAT update the live data
    /* format CODE --> format */
    private val formatLiveData = MutableLiveData<Map<String, SWCCGFormat>>()
    val formats: LiveData<Map<String, SWCCGFormat>>
        get() = formatLiveData

    init {
        formatLiveData.value = HashMap()

        GlobalScope.launch(Dispatchers.IO) {
            load()
        }
    }

    private suspend fun load() {
        var map: HashMap<String, String> = HashMap()
        var gempReader: Reader? = null

        try {
            val gempCards = pcService.getGempCards()
            gempReader = gempCards.body()?.charStream()
        } catch (e: Exception) {
            Log.e(TAG, "Error loading gemp card data from network: $e")
        }

        if (gempReader == null) {
            /* network failed, so load our backup from disk */
            try {
                //val inputStream = resources.openRawResource(R.raw.gemp_cards)
                //gempReader = BufferedReader(InputStreamReader(inputStream))
            } catch (e: Exception) {
                Log.e(TAG, "Error loading gemp card data from disk: $e")
            }
        }

        Log.e("catfat", "do we have a GMEP READER?  $gempReader")
        val regex = Regex("\":\"/gemp-swccg/images/cards/.*?/")

        gempReader?.let {
            it.forEachLine {
                /*
                val split = it.split(regex)
                split.forEach {
                    Log.e("atfat", " ---> $it")
                }
                 */
            }
        }
    }

    companion object {
        private val TAG = GempDataRepository::class.java.simpleName
    }
}
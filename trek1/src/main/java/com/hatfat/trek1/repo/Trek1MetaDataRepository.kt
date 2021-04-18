package com.hatfat.trek1.repo

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.hatfat.cards.data.CardsRepository
import com.hatfat.trek1.data.Trek1Card
import com.hatfat.trek1.data.Trek1Set
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class Trek1MetaDataRepository @Inject constructor(
    cardRepository: Trek1CardRepository
) : CardsRepository() {
    private val setsLiveData = MutableLiveData<HashMap<String, Trek1Set>>()
//    private val cardAlignmentsLiveData = MutableLiveData<Set<String>>()
//    private val keysLiveData = MutableLiveData<Set<String>>()

    val sets: LiveData<HashMap<String, Trek1Set>>
        get() = setsLiveData
//    val cardAlignments: LiveData<Set<String>>
//        get() = cardAlignmentsLiveData
//    val keys: LiveData<Set<String>>
//        get() = keysLiveData

//    private val specialKeys = HashSet<String>()
//    private val keysToIgnore = HashSet<String>()
//    private val keysToAdd = HashSet<String>()

    private val setsThatUseGif = HashSet<String>()
    private val setImageUrlMap = HashMap<String, String>()

    init {
        setImageUrlMap["Ref"] = "rfl"

//        setsThatUseGif.add("TV")
        setsThatUseGif.add("otsd")
//        setsThatUseGif.add("dom")
//        setsThatUseGif.add("sdII")
        setsThatUseGif.add("roa")
//        setsThatUseGif.add("BG")
//        setsThatUseGif.add("enterprise")
//        setsThatUseGif.add("En")
//        setsThatUseGif.add("DM")
        setsThatUseGif.add("ds9")
        setsThatUseGif.add("faj")
//        setsThatUseGif.add("FT")
//        setsThatUseGif.add("agt")
        setsThatUseGif.add("twt")
//        setsThatUseGif.add("SE")
//        setsThatUseGif.add("qc")
//        setsThatUseGif.add("tmp")
//        setsThatUseGif.add("X")
//        setsThatUseGif.add("CA")
        setsThatUseGif.add("mm")
//        setsThatUseGif.add("R2")
//        setsThatUseGif.add("voy")
//        setsThatUseGif.add("IMD")
//        setsThatUseGif.add("efc")
//        setsThatUseGif.add("SW")
        setsThatUseGif.add("2pg")
//        setsThatUseGif.add("CL")
//        setsThatUseGif.add("ep")
//        setsThatUseGif.add("borg")
        setsThatUseGif.add("premiere")
//        setsThatUseGif.add("AP")
//        setsThatUseGif.add("TWT")
//        setsThatUseGif.add("WYLB")
//        setsThatUseGif.add("Ref")
//        setsThatUseGif.add("1anth")
//        setsThatUseGif.add("Promo")
        setsThatUseGif.add("au")
//        setsThatUseGif.add("NE")
//        setsThatUseGif.add("awayteam")
//        setsThatUseGif.add("ha")
//        setsThatUseGif.add("armade")
        setsThatUseGif.add("bog")
//        setsThatUseGif.add("fc")
//        setsThatUseGif.add("2anth")
//        setsThatUseGif.add("Ge")
    }

    init {
        setsLiveData.value = HashMap()

        cardRepository.sortedCardsArray.observeForever {
            it?.let {
                GlobalScope.launch(Dispatchers.IO) {
                    load(it)
                }
            }
        }
    }

    private suspend fun load(cards: Array<Trek1Card>) {
        val setsHashSet = HashSet<String>()
        val newSets = HashMap<String, Trek1Set>()
//        val alignmentsHashSet = HashSet<String>()
//        val keysHashSet = HashSet<String>(keysToAdd)

        /* populate the metadata sets based on the cards we loaded */
        cards.forEach { card ->
            card.release?.let {
//                if (!setsHashSet.contains(it)) {
//                    Log.e("catfat", "first for $it ${card.name}")
//                }
                setsHashSet.add(it)
            }
        }

        setsHashSet.forEach {
//            Log.e("catfat", "set:$it")
            val imageUrlDir = setImageUrlMap[it] ?: it
            newSets[it] = Trek1Set(it, it, imageUrlDir, setsThatUseGif.contains(it))
        }

//        newSets.values.forEach {
//            Log.e("catfat", "set ${it.name} gif? ${it.isGif}")
//        }

        withContext(Dispatchers.Main) {
            setsLiveData.value = newSets

            loadedLiveData.value = true
        }
    }
}

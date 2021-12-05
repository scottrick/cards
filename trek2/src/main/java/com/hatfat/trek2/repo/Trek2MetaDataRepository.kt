package com.hatfat.trek2.repo

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.hatfat.cards.data.CardsRepository
import com.hatfat.trek2.data.Trek2Affiliation
import com.hatfat.trek2.data.Trek2Card
import kotlinx.coroutines.*
import javax.inject.Inject
import javax.inject.Singleton

@DelicateCoroutinesApi
@Singleton
class Trek2MetaDataRepository @Inject constructor(
    cardRepository: Trek2CardRepository
) : CardsRepository() {
    private val typesLiveData = MutableLiveData<HashSet<String>>()
    private val affiliationsLiveData = MutableLiveData<HashMap<String, Trek2Affiliation>>()

    val types: LiveData<HashSet<String>>
        get() = typesLiveData
    val affiliations: LiveData<HashMap<String, Trek2Affiliation>>
        get() = affiliationsLiveData

    init {
        typesLiveData.value = HashSet()
        affiliationsLiveData.value = HashMap()

        cardRepository.sortedCardsArray.observeForever {
            it?.let {
                GlobalScope.launch(Dispatchers.IO) {
                    load(it)
                }
            }
        }
    }

    private suspend fun load(cards: Array<Trek2Card>) {
        val newTypes = HashSet<String>()
        val newAffiliations = HashSet<String>()
        val newAffiliationsMap = HashMap<String, Trek2Affiliation>()

        /* populate the metadata based on the cards we loaded */
        cards.forEach { card ->
            card.type?.let {
                newTypes.add(it)
            }

            card.affiliation?.let {
                if (card.type == "Personnel" || card.type == "Ship") {
                    newAffiliations.add(it)
                }
            }
        }

        newAffiliations.forEach {
            val abbr = it.substring(0, 3)
            val abbrs = mutableListOf(abbr)

            if (abbr == "Non") {
                abbrs.add("NA")
            }

            val newAffiliation = Trek2Affiliation(it, abbrs)
            abbrs.forEach { affiliationAbbr ->
                newAffiliationsMap[affiliationAbbr] = newAffiliation
            }
        }

        withContext(Dispatchers.Main) {
            typesLiveData.value = newTypes
            affiliationsLiveData.value = newAffiliationsMap

            loadedLiveData.value = true
        }
    }
}
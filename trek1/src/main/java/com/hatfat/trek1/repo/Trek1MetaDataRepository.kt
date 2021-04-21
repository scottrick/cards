package com.hatfat.trek1.repo

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.hatfat.cards.data.CardsRepository
import com.hatfat.trek1.data.Trek1Card
import com.hatfat.trek1.data.Trek1Property
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
    private val typesLiveData = MutableLiveData<HashSet<String>>()
    private val affiliationsLiveData = MutableLiveData<HashSet<String>>()
    private val propertiesLiveData = MutableLiveData<HashMap<String, Trek1Property>>()

    val types: LiveData<HashSet<String>>
        get() = typesLiveData
    val affiliations: LiveData<HashSet<String>>
        get() = affiliationsLiveData
    val properties: LiveData<HashMap<String, Trek1Property>>
        get() = propertiesLiveData

    init {
        typesLiveData.value = HashSet()
        affiliationsLiveData.value = HashSet()
        propertiesLiveData.value = HashMap()

        cardRepository.sortedCardsArray.observeForever {
            it?.let {
                GlobalScope.launch(Dispatchers.IO) {
                    load(it)
                }
            }
        }
    }

    private suspend fun load(cards: Array<Trek1Card>) {
        val newTypes = HashSet<String>()
        val newAffiliations = HashSet<String>()
        val propertiesHashSet = HashSet<String>()
        val newProperties = HashMap<String, Trek1Property>()

        /* populate the metadata based on the cards we loaded */
        cards.forEach { card ->
            card.type?.let {
                if (it.startsWith("Q ")) {
                    newTypes.add("Q ")
                } else if (it.contains("/")) {
                    /* ignoring Interrupt/Event cards */
                } else {
                    newTypes.add(it)
                }
            }

            card.affiliation?.let {
                if (card.type == "Personnel" || card.type == "Ship" || card.type == "Facility") {
                    newAffiliations.add(it)
                }
            }

            card.property?.let {
                propertiesHashSet.add(it)
            }
        }

        propertiesHashSet.forEach {
            newProperties[it] = Trek1Property(it, it)
        }

        withContext(Dispatchers.Main) {
            typesLiveData.value = newTypes
            affiliationsLiveData.value = newAffiliations
            propertiesLiveData.value = newProperties

            loadedLiveData.value = true
        }
    }
}
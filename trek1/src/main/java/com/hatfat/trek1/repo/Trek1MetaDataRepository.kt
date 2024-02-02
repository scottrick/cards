package com.hatfat.trek1.repo

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.reflect.TypeToken
import com.hatfat.cards.data.CardsRepository
import com.hatfat.cards.data.loader.RawResourceLoader
import com.hatfat.trek1.R
import com.hatfat.trek1.data.Trek1Affiliation
import com.hatfat.trek1.data.Trek1Card
import com.hatfat.trek1.data.Trek1Property
import com.hatfat.trek1.search.filter.affil.Trek1AffiliationOption
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class Trek1MetaDataRepository @Inject constructor(
    private val cardRepository: Trek1CardRepository,
    private val rawResourceLoader: RawResourceLoader,
) : CardsRepository() {
    private val typesLiveData = MutableLiveData<HashSet<String>>()
    private val affiliationsLiveData = MutableLiveData<HashSet<Trek1Affiliation>>()
    private val affiliationOptionsLiveData = MutableLiveData<HashSet<Trek1AffiliationOption>>()
    private val propertiesLiveData = MutableLiveData<HashMap<String, Trek1Property>>()

    val types: LiveData<HashSet<String>>
        get() = typesLiveData
    val affiliations: LiveData<HashSet<Trek1Affiliation>>
        get() = affiliationsLiveData
    val affiliationOptions: LiveData<HashSet<Trek1AffiliationOption>>
        get() = affiliationOptionsLiveData
    val properties: LiveData<HashMap<String, Trek1Property>>
        get() = propertiesLiveData

    init {
        typesLiveData.value = HashSet()
        affiliationsLiveData.value = HashSet()
        propertiesLiveData.value = HashMap()

    }

    override fun setup() {
        cardRepository.sortedCardsArray.observeForever {
            it?.let {
                coroutineScope.launch(coroutineDispatcher) {
                    load(it)
                }
            }
        }
    }

    private suspend fun load(cards: Array<Trek1Card>) {
        val newTypes = HashSet<String>()
        val newAffiliations = HashSet<String>()
        val newAffiliationOptions = HashSet<Trek1AffiliationOption>()
        val newProperties = HashMap<String, Trek1Property>()

        val typeToken = object : TypeToken<List<Trek1Property>>() {}
        val properties = rawResourceLoader.load(R.raw.properties, typeToken)

        Log.i(TAG, "Loaded ${properties.size} properties.")

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
        }

        val affiliations = HashSet<Trek1Affiliation>()
        newAffiliations.forEach {
            if (it.contains("/")) {
                return@forEach
            }

            val prefix = it.substring(0, 3).uppercase(Locale.getDefault())
            affiliations.add(Trek1Affiliation(it, prefix))
        }

        newAffiliations.forEach { affiliation ->
            val affiliationsList = affiliation.split("/").mapNotNull { affiliationName ->
                affiliations.find {
                    it.displayName == affiliationName
                }
            }

            if (affiliationsList.isNotEmpty()) {
                newAffiliationOptions.add(Trek1AffiliationOption(affiliation, affiliationsList))
            }
        }

        properties.forEach {
            newProperties[it.code] = it
        }

        withContext(Dispatchers.Main) {
            typesLiveData.value = newTypes
            affiliationsLiveData.value = affiliations
            affiliationOptionsLiveData.value = newAffiliationOptions
            propertiesLiveData.value = newProperties

            loadedLiveData.value = true
        }
    }

    companion object {
        private val TAG = Trek1MetaDataRepository::class.java.simpleName
    }
}
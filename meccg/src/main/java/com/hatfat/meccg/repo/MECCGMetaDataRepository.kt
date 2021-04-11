package com.hatfat.meccg.repo

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.hatfat.cards.data.CardsRepository
import com.hatfat.meccg.data.MECCGCard
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MECCGMetaDataRepository @Inject constructor(
    cardRepository: MECCGCardsRepository
) : CardsRepository() {
    private val cardTypesLiveData = MutableLiveData<Set<String>>()
    private val cardAlignmentsLiveData = MutableLiveData<Set<String>>()
    private val keysLiveData = MutableLiveData<Set<String>>()

    val cardTypes: LiveData<Set<String>>
        get() = cardTypesLiveData
    val cardAlignments: LiveData<Set<String>>
        get() = cardAlignmentsLiveData
    val keys: LiveData<Set<String>>
        get() = keysLiveData

    val specialKeys = HashSet<String>()

    init {
        specialKeys.add("Stolen Knowledge")
        specialKeys.add("Spirit Ring")
        specialKeys.add("Sea Serpent")
        specialKeys.add("One Ring")
        specialKeys.add("Mind Ring")
        specialKeys.add("Man Ring")
        specialKeys.add("Magic Ring")
        specialKeys.add("Lost Knowledge")
        specialKeys.add("Light Enchantment")
        specialKeys.add("Lesser Ring")
        specialKeys.add("Gold Ring")
        specialKeys.add("Elven Ring")
        specialKeys.add("Dwarven Ring")
        specialKeys.add("Dark Enchantment")
        specialKeys.add("Awakened Plant")
    }

    init {
        cardTypesLiveData.value = HashSet()
        cardAlignmentsLiveData.value = HashSet()
        keysLiveData.value = HashSet()

        cardRepository.sortedCardsArray.observeForever {
            it?.let {
                GlobalScope.launch(Dispatchers.IO) {
                    load(it)
                }
            }
        }
    }

    private suspend fun load(cards: Array<MECCGCard>) {
        val typesHashSet = HashSet<String>()
        val alignmentsHashSet = HashSet<String>()
        val keysHashSet = HashSet<String>()

        /* populate the metadata sets based on the cards we loaded */
        for (card in cards) {
            if (!card.primary.isNullOrBlank()) {
                typesHashSet.add(card.primary.trim())
            }

            if (!card.alignment.isNullOrBlank()) {
                alignmentsHashSet.add(card.alignment.trim())
            }

            if (!card.race.isNullOrBlank()) {
                var keys: String = card.race
                specialKeys.forEach {
                    if (keys.contains(it, true)) {
                        keysHashSet.add(it)
                        val startIndex = keys.indexOf(it)
                        keys = keys.removeRange(startIndex, startIndex + it.length)
                    }
                }

                keys = keys.trim()
                keys.split(" ").forEach {
                    keysHashSet.add(it)
                }
            }
        }

        withContext(Dispatchers.Main) {
            cardTypesLiveData.value = typesHashSet
            cardAlignmentsLiveData.value = alignmentsHashSet
            keysLiveData.value = keysHashSet
            loadedLiveData.value = true
        }
    }
}

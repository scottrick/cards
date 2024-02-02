package com.hatfat.trek1.inject

import com.hatfat.cards.app.CardsConfig
import com.hatfat.cards.data.DataReady
import com.hatfat.cards.results.general.SearchResultsDataProvider
import com.hatfat.cards.search.CardSearchHandler
import com.hatfat.cards.search.CardSearchOptionsProvider
import com.hatfat.trek1.app.Trek1CardsConfig
import com.hatfat.trek1.data.Trek1DataReady
import com.hatfat.trek1.results.Trek1SearchResultsDataProvider
import com.hatfat.trek1.search.Trek1CardSearchOptionsProvider
import com.hatfat.trek1.search.Trek1SearchHandler
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Suppress("unused")
@Module
@InstallIn(SingletonComponent::class)
abstract class Trek1Module {

    @Binds
    abstract fun bindDataReady(
        dataReady: Trek1DataReady
    ): DataReady

    @Binds
    abstract fun bindCardsConfig(
        config: Trek1CardsConfig
    ): CardsConfig

    @Binds
    abstract fun bindSearchOptionsProvider(
        trek1SearchOptionsProvider: Trek1CardSearchOptionsProvider
    ): CardSearchOptionsProvider

    @Binds
    abstract fun bindSearchHandler(
        trek1SearchHandler: Trek1SearchHandler
    ): CardSearchHandler

    @Binds
    abstract fun bindSearchResultsDataProvider(
        dataProvider: Trek1SearchResultsDataProvider
    ): SearchResultsDataProvider
}
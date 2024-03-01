package com.hatfat.swccg.inject

import com.hatfat.cards.app.CardsConfig
import com.hatfat.cards.data.DataReady
import com.hatfat.cards.results.SearchResultsSerializer
import com.hatfat.cards.results.general.SearchResultsDataProvider
import com.hatfat.cards.search.CardSearchHandler
import com.hatfat.cards.search.CardSearchOptionsProvider
import com.hatfat.swccg.app.SWCCGCardsConfig
import com.hatfat.swccg.data.SWCCGDataReady
import com.hatfat.swccg.results.SWCCGSearchResultsDataProvider
import com.hatfat.swccg.results.SWCCGSearchResultsSerializer
import com.hatfat.swccg.search.SWCCGCardSearchOptionsProvider
import com.hatfat.swccg.search.SWCCGSearchHandler
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Suppress("unused")
@Module
@InstallIn(SingletonComponent::class)
abstract class SWCCGModule {

    @Binds
    abstract fun bindDataReady(
        dataReady: SWCCGDataReady
    ): DataReady

    @Binds
    abstract fun bindCardsConfig(
        config: SWCCGCardsConfig
    ): CardsConfig

    @Binds
    abstract fun bindSearchOptionsProvider(
        swccgSearchOptionsProvider: SWCCGCardSearchOptionsProvider
    ): CardSearchOptionsProvider

    @Binds
    abstract fun bindSearchHandler(
        swccgSearchHandler: SWCCGSearchHandler
    ): CardSearchHandler

    @Binds
    abstract fun bindSearchResultsDataProvider(
        dataProvider: SWCCGSearchResultsDataProvider
    ): SearchResultsDataProvider

    @Binds
    abstract fun bindSearchResultsSerializer(
        serializer: SWCCGSearchResultsSerializer
    ): SearchResultsSerializer
}

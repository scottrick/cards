package com.hatfat.meccg.inject

import com.hatfat.cards.app.CardsConfig
import com.hatfat.cards.data.DataReady
import com.hatfat.cards.results.SearchResultsSerializer
import com.hatfat.cards.results.general.SearchResultsDataProvider
import com.hatfat.cards.search.CardSearchHandler
import com.hatfat.cards.search.CardSearchOptionsProvider
import com.hatfat.meccg.app.MECCGCardsConfig
import com.hatfat.meccg.data.MECCGDataReady
import com.hatfat.meccg.results.MECCGSearchResultsDataProvider
import com.hatfat.meccg.results.MECCGSearchResultsSerializer
import com.hatfat.meccg.search.MECCGCardSearchOptionsProvider
import com.hatfat.meccg.search.MECCGSearchHandler
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Suppress("unused")
@Module
@InstallIn(SingletonComponent::class)
abstract class MECCGModule {

    @Binds
    abstract fun bindDataReady(
        dataReady: MECCGDataReady
    ): DataReady

    @Binds
    abstract fun bindCardsConfig(
        config: MECCGCardsConfig
    ): CardsConfig

    @Binds
    abstract fun bindSearchOptionsProvider(
        meccgSearchOptionsProvider: MECCGCardSearchOptionsProvider
    ): CardSearchOptionsProvider

    @Binds
    abstract fun bindSearchHandler(
        meccgSearchHandler: MECCGSearchHandler
    ): CardSearchHandler

    @Binds
    abstract fun bindSearchResultsDataProvider(
        dataProvider: MECCGSearchResultsDataProvider
    ): SearchResultsDataProvider

    @Binds
    abstract fun bindSearchResultsSerializer(
        serializer: MECCGSearchResultsSerializer
    ): SearchResultsSerializer
}